package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.BookRequest;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.BookRequestService;

@Service
public class SendEmailForManuscriptUploadService implements JavaDelegate {

    @Autowired
    private EmailService emailService;
    @Autowired
    private BookRequestService bookRequestService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Usao u slanje mejla zbog isteka roka za upload manuscripta.");

        Long bookRequestId = (Long) execution.getVariable("bookRequestId");
        BookRequest br = this.bookRequestService.findById(bookRequestId);
        br.setAccepted(false);
        this.bookRequestService.save(br);

        String subject = "Book request rejected.";
        String text = "Hello " + br.getWriter().getFirstName() + " " + br.getWriter().getLastName() + ",\n\nYour request for book publishing has been rejected because you haven't uploaded the manuscript in time." +
                "\n\nBest regards,\nLiterary association";

        emailService.sendEmail(br.getWriter().getEmail(), subject, text);
    }
}
