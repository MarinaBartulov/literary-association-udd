package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.List;

@Service
public class RemoveNewUserService implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        //ova funkcija ce se moci koristiti i za pisca
        System.out.println("Usao u RemoveNewUserService");
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");

        String username = "";
        for(FormSubmissionDTO f: formData){
            if(f.getFieldId().equals("username")){
                username = (String)f.getFieldValue();
            }
        }
        User notActivatedUser = this.userService.findByUsername(username);
        if(notActivatedUser != null){
            this.userService.deleteUser(notActivatedUser);
            this.identityService.deleteUser(username);
        }
    }
}
