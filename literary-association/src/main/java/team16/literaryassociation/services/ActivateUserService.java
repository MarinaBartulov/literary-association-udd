package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.User;
import team16.literaryassociation.model.VerificationToken;
import team16.literaryassociation.services.interfaces.UserService;
import team16.literaryassociation.services.interfaces.VerificationTokenService;

@Service
public class ActivateUserService implements JavaDelegate {

    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Uslo u activate account.");

        String token = (String) execution.getVariable("token");
        VerificationToken vt = this.verificationTokenService.findToken(token);

        if(vt == null){
            System.out.println("Token is null");
            throw new BpmnError("ACCOUNT_ACTIVATION_FAILED", "Account activation failed. Token expired.");
        }
        if(!vt.isValid()){
            throw new BpmnError("ACCOUNT_ACTIVATION_FAILED", "Account activation failed. Token expired.");
        }

        User user = vt.getUser();
        user.setVerified(true);
        user.setEnabled(true);
        execution.setVariable("user_id", user.getId());
        this.userService.saveUser(user);
    }
}
