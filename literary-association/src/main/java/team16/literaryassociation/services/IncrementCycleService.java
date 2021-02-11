package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.feel.syntaxtree.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class IncrementCycleService implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u increment cycle");

        Integer cyclesNumber = (Integer) execution.getVariable("cycleNumber");
        cyclesNumber ++;
        execution.setVariable("cycleNumber", cyclesNumber);

        //za email o potrebi da upload-uje jos radova
        String username = (String) execution.getVariable("username");
        User user = this.userService.findByUsername(username);
        String text = "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\nSome of the board members have asked you to give" +
                "them more material, so that they can make their decision. You have two days to upload new work or else your membership application will be rejected.\n" +
                "\n\nBest regards,\nLiterary association";

        String subject = "Literary association - Membership application";
        execution.setVariable("email", user.getEmail());
        execution.setVariable("emailSubject", subject);
        execution.setVariable("emailText", text);
    }
}
