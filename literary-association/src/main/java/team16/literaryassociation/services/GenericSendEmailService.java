package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.services.impl.EmailService;

@Service
public class GenericSendEmailService implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String email = (String) execution.getVariable("email");
        String text = (String) execution.getVariable("emailText");
        String subject = (String) execution.getVariable("emailSubject");
        emailService.sendEmail(email, subject, text);

    }
}
