package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class SendEmailToMainEditorService implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Uslo u SendEmailToMainEditorService");
        String username = (String) execution.getVariable("mainEditor");
        System.out.println("Main editor: " + username);

        User user = userService.findByUsername(username);
        if (user == null) {
            System.out.println("Nije nasao main editora");
            throw new BpmnError("SENDING_EMAIL_FAILED", "Sending email to main editor failed.");
        }

        String text = "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\nBoard members didn't agree whether book is plagiat or not,  " +
                "so you will again have to choose editors for review of the book."
                + "\n\nBest regards,\nLiterary association";

        String subject = "Plagiarism detection.";

        try {
            emailService.sendEmail(user.getEmail(), subject, text);
        } catch (Exception e) {
            throw new BpmnError("SENDING_EMAIL_FAILED", "Sending email to main editor failed.");
        }
    }
}
