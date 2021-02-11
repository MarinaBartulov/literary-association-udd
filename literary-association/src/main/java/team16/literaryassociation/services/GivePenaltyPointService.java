package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.BetaReaderDTO;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.ReaderService;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class GivePenaltyPointService implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReaderService readerService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("Usao u GivePenaltyPointService");

        BetaReaderDTO betaReader =  (BetaReaderDTO) delegateExecution.getVariable("betaReader");
        Reader reader = (Reader) userService.findByUsername(betaReader.getUsername());

        reader.setPenaltyPoints(reader.getPenaltyPoints() + 1);
        Reader saved = readerService.saveReader(reader);

        if(saved.getPenaltyPoints() > 4) {
            saved.setBetaReader(false);
            readerService.saveReader(saved);

            String subject = "Beta-Reader status loss";
            String text = "Hello " + saved.getFirstName() + " " + saved.getLastName() + ",\n\nYou" +
                    "'ve lost your beta-reader status, because you haven got five penalty points." +
                    "Beta-reader status cannot be restored." +
                    "\n\nBest regards,\nLiterary association";

            emailService.sendEmail(saved.getEmail(), subject, text);
        }

    }
}
