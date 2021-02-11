package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.EditorDTO;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.mapper.EditorMapper;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.model.PlagiarismComplaint;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.EditorService;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SaveNewEditorService implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private EditorService editorService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private EditorMapper editorMapper = new EditorMapper();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) delegateExecution.getVariable("formData");
        Map<String, Object> map = listFieldsToMap(formData);

        Long plagiarismComplaintId = (Long) delegateExecution.getVariable("plagiarismComplaintId");
        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.findById(plagiarismComplaintId);
        if(plagiarismComplaint == null)
        {
            System.out.println("Nije nasao PlagiarismComplaint");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Plagiarism complaint doesn't exist.");
            throw new BpmnError("SAVING_NEW_EDITOR_FAILED", "Saving new editor failed.");
        }

        Book myBook = plagiarismComplaint.getMyBook();

        String editorId = (String) map.get("remainingEditors");
        Editor e = editorService.findById(Long.parseLong(editorId));
        if(e == null) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Chosen editor could not be found.");
            throw new BpmnError("SAVING_NEW_EDITOR_FAILED", "Saving new editor failed.");
        }
        myBook.getOtherEditors().add(e);

        // da li brisemo onog koji se menja?
        String badEditor = (String) map.get("timeExpiredEditor");
        User user = userService.findByUsername(badEditor);
        if( user == null ) {
            System.out.println("Nije sacuvao Editora");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Chosen editor could not be found.");
            throw new BpmnError("SAVING_NEW_EDITOR_FAILED", "Saving new editor failed.");
        }
        Editor timeExpEditor = (Editor) user;
        myBook.getOtherEditors().remove(timeExpEditor);

        try {
            bookService.save(myBook);
        } catch (Exception exception) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Saving book with new editor failed.");
            throw new BpmnError("SAVING_NEW_EDITOR_FAILED", "Saving new editor failed.");
        }

        EditorDTO newEditorDTO = editorMapper.toDto(e);
        delegateExecution.setVariable("editor", newEditorDTO);
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
