package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.MembershipApplication;
import team16.literaryassociation.model.User;
import team16.literaryassociation.model.VerificationToken;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.MembershipApplicationService;
import team16.literaryassociation.services.interfaces.UserService;
import team16.literaryassociation.services.interfaces.VerificationTokenService;

@Service
public class ActivateWriterService implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipApplicationService membershipApplicationService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Uslo u activate account.");

        String token = (String) execution.getVariable("token");
        VerificationToken vt = this.verificationTokenService.findToken(token);

        if(vt == null){
            System.out.println("Token is null");
            throw new BpmnError("ACCOUNT_ACTIVATION_FAILED", "Account activation failed. Token is null.");
        }
        if(!vt.isValid()){
            System.out.println("Token is not valid");
            throw new BpmnError("ACCOUNT_ACTIVATION_FAILED", "Account activation failed. Token expired.");
        }

        User user = vt.getUser();
        user.setVerified(true);
        user.setEnabled(true);
        execution.setVariable("user_id", user.getId());

        userService.saveUser(user);

        Writer writer = (Writer)userService.findByUsername(user.getUsername());
        if(writer== null)
        {
            throw new BpmnError("ACCOUNT_ACTIVATION_FAILED", "Creating membership application failed, writer not found");
        }

        String processId = execution.getProcessInstanceId();
        try{
            MembershipApplication membershipApplication = membershipApplicationService.save(new MembershipApplication(writer, processId));
            execution.setVariable("membership_application_id", membershipApplication.getId());
        }catch (Exception e){
            throw new BpmnError("ACCOUNT_ACTIVATION_FAILED", "Creating membership application failed.");
        }


    }
}
