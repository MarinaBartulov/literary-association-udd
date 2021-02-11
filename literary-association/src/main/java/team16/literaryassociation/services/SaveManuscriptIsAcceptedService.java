package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.ManuscriptService;

@Service
public class SaveManuscriptIsAcceptedService implements JavaDelegate {

    @Autowired
    private ManuscriptService manuscriptService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Usao u save manuscript is accepted");

        Long manuscriptId = (Long) execution.getVariable("manuscriptId");
        Manuscript manuscript = this.manuscriptService.findById(manuscriptId);
        if(manuscript == null){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", "Saving editors decision failed. Accept or reject again.");
            throw new BpmnError("SAVING_MANUSCRIPT_ACCEPTED_FAILED");
        }
        boolean accept = (boolean) execution.getVariable("accept");
        manuscript.setAccepted(accept);

        String reasonForRejection = "";
        if(!accept){
            reasonForRejection = (String) execution.getVariable("reasonForRejection");
            if(reasonForRejection.trim().equals("")){
                execution.setVariable("globalError", true);
                execution.setVariable("globalErrorMessage", "Reason for rejection is required.");
                throw new BpmnError("SAVING_MANUSCRIPT_ACCEPTED_FAILED");
            }
            manuscript.setReasonForRejection(reasonForRejection);
        }

        try {
            this.manuscriptService.save(manuscript);
        }catch(Exception e){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", "Saving editors decision failed. Accept or reject again.");
            throw new BpmnError("SAVING_MANUSCRIPT_ACCEPTED_FAILED");
        }

        if(!accept){
            System.out.println("Salje se mejl da je odbijen manuscript");
            Writer writer = manuscript.getBookRequest().getWriter();
            String email = writer.getEmail();
            String subject = "Manuscript rejected.";
            String text = "Hello " + writer.getFirstName() + " " + writer.getLastName() + ",\n\nYour manuscript has been rejected. Reason for rejection: " + reasonForRejection +
                    "\n\nBest regards,\nLiterary association";
            execution.setVariable("email", email);
            execution.setVariable("emailText", text);
            execution.setVariable("emailSubject", subject);
        }
    }
}
