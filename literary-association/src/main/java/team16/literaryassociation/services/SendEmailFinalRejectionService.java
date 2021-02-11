package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.ManuscriptService;

@Service
public class SendEmailFinalRejectionService implements JavaDelegate {

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u SendEmailFinalRejectionService");

        Long manuscriptId = (Long) delegateExecution.getVariable("manuscriptId");
        Manuscript manuscript = manuscriptService.findById(manuscriptId);
        if(manuscript == null) {
            System.out.println("Nije nasao Manuscript");
            return;
        }

        System.out.println("Salje se mejl piscu da je odbijen za izdavanje knjige");
        String subject = "Manuscript rejected";
        String text = "Hello " + manuscript.getBookRequest().getWriter().getFirstName() + " " +
                manuscript.getBookRequest().getWriter().getLastName() + ",\n\nYour " +
                "manuscript for book publishing has been rejected, because you haven't uploaded new manuscript " +
                "according to editor's suggestions on time." +
                "\n\nBest regards,\nLiterary association";

        emailService.sendEmail(manuscript.getBookRequest().getWriter().getEmail(), subject, text);
    }
}
