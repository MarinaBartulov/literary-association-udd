package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class SaveWritersRejectionService implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Usao u Save Writers Rejection");

        String username = (String) execution.getVariable("username");
        User user = this.userService.findByUsername(username);
        if(user == null)
        {
            throw new BpmnError("WRITER_REJECTION_FAILED", "Writer not found.");
        }

        user.setEnabled(false);
        User savedUser = userService.saveUser(user);
        if(savedUser == null)
        {
            throw new BpmnError("WRITER_REJECTION_FAILED", "Error saving user");
        }
    }
}
