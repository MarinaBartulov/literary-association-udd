package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.services.interfaces.GenreService;
import team16.literaryassociation.services.interfaces.UserService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WriterValidationService implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u WriterValidationService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);
        boolean isValid = true;
        String errorMsg = "";

        for(FormSubmissionDTO item: formData){
            System.out.println(item.getFieldId() + " : " + item.getFieldValue());
        }

        if(userService.findByUsername((String)map.get("username")) != null){
            isValid = false;
            errorMsg += "This username already exists. ";
        }
        if(!isValidEmailAddress((String) map.get("email"))) {
            isValid = false;
            errorMsg += "Email is not valid. ";
        }
        if(userService.findByEmail((String)map.get("email"))!=null){
            isValid = false;
            errorMsg += "Email already exists.";
        }
        if(!map.get("password").equals(map.get("confirmPassword"))){
            isValid = false;
            errorMsg += "Passwords don't match. ";
        }

        List<String> genres = (List<String>) map.get("genres");
        if(genres.size() < 1){
            isValid = false;
            errorMsg += "At least one genre is required. ";
        }
        for(String genre : genres){
            if(this.genreService.findByName(genre) == null){
                isValid = false;
                break;
            }
        }
        if(!isValid){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", errorMsg);
        }

        execution.setVariable("isValid", isValid);
        System.out.println("postavio isValid na " + isValid);
    }

    private Map<String, Object> listFieldsToMap(List<FormSubmissionDTO> formData) {
        Map<String, Object> retVal = new HashMap<>();
        for(FormSubmissionDTO dto: formData) {
            if(dto.getFieldId() != null) {
                retVal.put(dto.getFieldId(), dto.getFieldValue());
            }
        }
        return retVal;
    }


    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
