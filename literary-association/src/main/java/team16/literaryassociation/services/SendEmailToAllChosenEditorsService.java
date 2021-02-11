package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.model.PlagiarismComplaint;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;

@Service
public class SendEmailToAllChosenEditorsService implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u send email to all chosen editors");

        Long plagiarismComplaintId = (Long) delegateExecution.getVariable("plagiarismComplaintId");

        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.findById(plagiarismComplaintId);
        if(plagiarismComplaint == null) {
            System.out.println("Nije nasao plagiarism complaint");
            throw new BpmnError("SENDING_EMAIL_FAILED", "Sending email to all chosen editors failed.");
        }

        Book myBook = plagiarismComplaint.getMyBook();

        String subject = "Plagiarism detection notes";
        for (Editor e: myBook.getOtherEditors()) {
            String content = "Dear " + e.getFirstName() + " " + e.getLastName() +
                    ", \n\n You have been chosen to give your notes about reported plagiarism." +
                    "Please, check your task list to give your opinion. \n\n" +
                    "Best regards,\nLiterary association";
            try {
                emailService.sendEmail(e.getEmail(), subject, content);
            } catch (Exception e1) {
                throw new BpmnError("SENDING_EMAIL_FAILED", "Sending email to all chosen editors failed.");
            }
        }
    }
}
