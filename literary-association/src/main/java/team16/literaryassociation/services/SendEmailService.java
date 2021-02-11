package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.UserService;
import team16.literaryassociation.services.interfaces.VerificationTokenService;

import java.util.UUID;

@Service
public class SendEmailService implements JavaDelegate {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u EmailService");
        String errorMsg = "";

        String username = (String) execution.getVariable("username");
        User user = this.userService.findByUsername(username);
        if (user == null) {
            System.out.println("Nije nasao usera");
            errorMsg = "Error sending email, user not found";
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", errorMsg);
            throw new BpmnError("SENDING_EMAIL_FAILED", "Error sending email, User not found");
        }

        String token = UUID.randomUUID().toString();
        this.verificationTokenService.createVerificationToken(user, token);

        String confirmationUrl = "https://localhost:3000/registrationConfirmation/" + execution.getProcessInstanceId() + "/" + token;

        String text = "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\nPlease confirm your registration by clicking on the link below: " +
                " \n" + confirmationUrl + "\n\nBest regards,\nLiterary association";

        String subject = "Literary association account activation.";

        try {
            emailService.sendEmail(user.getEmail(), subject, text);
        } catch (Exception e)
        {
            System.out.println("Error sending email");
            errorMsg = "Error sending email";
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", errorMsg);
            throw new BpmnError("SENDING_EMAIL_FAILED", "Error sending email");
        }


    }
}
