package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.BookRequest;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.ManuscriptService;

@Service
public class SaveEditorsFinalDecisionService implements JavaDelegate {

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u SaveEditorsFinalDecisionService");

        boolean approved = (boolean) delegateExecution.getVariable("editorsApproval");
        String suggestions = (String) delegateExecution.getVariable("suggestions");

        Long manuscriptId = (Long) delegateExecution.getVariable("manuscriptId");
        Manuscript manuscript = manuscriptService.findById(manuscriptId);
        if(manuscript == null) {
            System.out.println("Nije nasao Manuscript");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Manuscript cannot be found.");
            throw new BpmnError("FINAL_EDITOR_DECISION_FAILED", "Saving editor's final decision failed.");
        }
        manuscript.setFinalEditorsApproval(approved);
        try {
            manuscriptService.save(manuscript);
        } catch (Exception e) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Saving editor's final decision failed.");
            throw new BpmnError("FINAL_EDITOR_DECISION_FAILED", "Saving editor's final decision failed.");
        }

        if(!approved) {
            try {
                manuscript.setSuggestions(suggestions);
            } catch (Exception e) {
                delegateExecution.setVariable("globalError", true);
                delegateExecution.setVariable("globalErrorMessage", "Saving editor's final decision failed.");
                throw new BpmnError("FINAL_EDITOR_DECISION_FAILED", "Saving editor's final decision failed.");
            }

            System.out.println("Salje se mejl da je odbio i koje su mu sugestije");
            String subject = "Manuscript rejected with suggestions";
            String text = "Hello " + manuscript.getBookRequest().getWriter().getFirstName() + " " +
                    manuscript.getBookRequest().getWriter().getLastName() + ",\n\nYour " +
                    "manuscript for book publishing has been rejected. Editor's suggestions: " + suggestions +
                    "\n\nBest regards,\nLiterary association";

            emailService.sendEmail(manuscript.getBookRequest().getWriter().getEmail(), subject, text);
        }

        delegateExecution.setVariable("editorsApproval", approved);
    }
}
