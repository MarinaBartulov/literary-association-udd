package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.MembershipApplication;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.impl.EmailService;
import team16.literaryassociation.services.interfaces.MembershipApplicationService;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class SendApprovedEmailToWriterService implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipApplicationService membershipApplicationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u SendEApprovedEmailToWriterService");
        String username = (String) execution.getVariable("username");
        User user = this.userService.findByUsername(username);

        String text = "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\nWe are pleased to inform you that your membership application has been accepted" +
                " by our board members.\n In order to confirm your membership you need to pay for membership fee, or else your application will be rejected."
                + "\n\nBest regards,\nLiterary association";

        String subject = "Literary association membership application.";

        emailService.sendEmail(user.getEmail(), subject, text);

        Long id = (Long)execution.getVariable("membership_application_id");
        MembershipApplication membershipApplication = membershipApplicationService.getOne(id);
        if(membershipApplication == null)
        {
            System.out.println("Membership application not found");
            //throw error
        }
        membershipApplication.setApprovedForPaying(true);
        membershipApplicationService.save(membershipApplication);

        // u slucaju da ne plati na vreme clanarinu
        text = "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\nYour deadline for paying membership fee has expired." +
                "\n Your membership application has been rejected. \n Don't contact us ever again!!!1" +
                "\n\nBest regards,\nLiterary association";

        execution.setVariable("email", user.getEmail());
        execution.setVariable("emailSubject", subject);
        execution.setVariable("emailText", text);
    }
}
