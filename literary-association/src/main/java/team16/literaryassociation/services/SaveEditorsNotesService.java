package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.model.EditorPlagiarismNote;
import team16.literaryassociation.model.PlagiarismComplaint;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.interfaces.EditorPlagiarismNoteService;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SaveEditorsNotesService implements JavaDelegate {

    @Autowired
    private EditorPlagiarismNoteService editorPlagiarismNoteService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("Usao u SaveEditorsNotesService");

        String username = identityService.getCurrentAuthentication().getUserId();
        User user = userService.findByUsername(username);
        if (user == null) {
            System.out.println("Nije nasao editora");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "You are not authenticated.");
            throw new BpmnError("SAVING_EDITORS_NOTES_FAILED", "Saving editors notes failed.");
        }
        Editor editor = (Editor) user;

        Long plagiarismComplaintId = (Long) delegateExecution.getVariable("plagiarismComplaintId");
        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.findById(plagiarismComplaintId);
        if(plagiarismComplaint == null) {
            System.out.println("Nije nasao plagiarism complaint");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Plagiarism complaint doesn't exist.");
            throw new BpmnError("SAVING_EDITORS_NOTES_FAILED", "Saving editors notes failed.");
        }

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) delegateExecution.getVariable("formData");
        Map<String, Object> map = listFieldsToMap(formData);

        EditorPlagiarismNote note = new EditorPlagiarismNote();
        note.setPlagiarismComplaint(plagiarismComplaint);
        note.setEditor(editor);
        note.setNotes((String) map.get("notes"));
        try {
            EditorPlagiarismNote editorNote = editorPlagiarismNoteService.save(note);
            Set<EditorPlagiarismNote> notes = plagiarismComplaint.getEditorPlagiarismNotes();
            notes.add(editorNote);
            plagiarismComplaint.setEditorPlagiarismNotes(notes);
            plagiarismComplaintService.save(plagiarismComplaint);
        } catch (Exception e) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Saving editors notes failed.");
            throw new BpmnError("SAVING_EDITORS_NOTES_FAILED", "Saving editors notes failed.");
        }
    }

    private Map<String, Object> listFieldsToMap(List<FormSubmissionDTO> formData) {
        Map<String, Object> retVal = new HashMap<>();
        for(FormSubmissionDTO dto: formData) {
            if(dto.getFieldId() != null) {
                retVal.put(dto.getFieldId(), dto.getFieldValue());
            }
        }
        return retVal;
    }
}
