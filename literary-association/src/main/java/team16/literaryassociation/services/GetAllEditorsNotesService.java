package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.EditorPlagiarismNote;
import team16.literaryassociation.model.PlagiarismComplaint;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;

@Service
public class GetAllEditorsNotesService implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long plagiarismComplaintId = (Long) delegateExecution.getVariable("plagiarismComplaintId");

        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.findById(plagiarismComplaintId);
        if(plagiarismComplaint == null) {
            System.out.println("Nije nasao plagiarism complaint");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Plagiarism complaint doesn't exist.");
            throw new BpmnError("GETTING_EDITORS_NOTES_FAILED", "Saving editors notes failed.");
        }

        String notes = "";
        int i = 0;
        for (EditorPlagiarismNote epn: plagiarismComplaint.getEditorPlagiarismNotes()) {
            i++;
            notes += "Note " + i + ": \n" + epn.getNotes() + "\n\n";
        }

        delegateExecution.setVariable("allEditorsNotes", notes);
        //za novi krug brojanja misljenja
        delegateExecution.setVariable("plagiat", 0);
        delegateExecution.setVariable("notPlagiat",0);
    }
}
