package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.model.Role;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.GenreService;
import team16.literaryassociation.services.interfaces.RoleService;
import team16.literaryassociation.services.interfaces.WriterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveWriterService implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private WriterService writerService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Uslo u SaveWriterService");
        String errorMsg = "";

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Writer newWriter = new Writer();
        newWriter.setFirstName((String)map.get("firstName"));
        newWriter.setLastName((String)map.get("lastName"));
        newWriter.setEmail((String) map.get("email"));
        System.out.println(passwordEncoder.encode((String) map.get("password")));
        newWriter.setPassword(passwordEncoder.encode((String) map.get("password")));
        Role role = this.roleService.findByName("ROLE_WRITER");
        newWriter.getRoles().add(role);
        newWriter.setUsername((String) map.get("username"));
        newWriter.setCity((String) map.get("city"));
        newWriter.setCountry((String) map.get("country"));

        List<String> genres = (List<String>) map.get("genres");
        for (String genre : genres) {
            Genre g = this.genreService.findByName(genre);
            if(g != null){
                newWriter.getGenres().add(g);
            }
        }

        try {
            writerService.saveWriter(newWriter);
            System.out.println("Sacuvao pisca");
            org.camunda.bpm.engine.identity.User cmdUser = identityService.newUser(newWriter.getUsername());
            cmdUser.setEmail(newWriter.getEmail());
            cmdUser.setFirstName(newWriter.getFirstName());
            cmdUser.setLastName(newWriter.getLastName());
            cmdUser.setPassword(newWriter.getPassword());
            try {
                identityService.saveUser(cmdUser);
                System.out.println("Sacuvao camunda usera");
            }catch(Exception e){
                System.out.println("Nije Sacuvao camunda usera");
                errorMsg="Saving camunda user failed";
                execution.setVariable("globalError", true);
                execution.setVariable("globalErrorMessage", errorMsg);
                throw new BpmnError("SAVE_USER_FAILED", "Saving camunda user failed.");
            }
        }
        catch(Exception e){
            System.out.println(" Nije Sacuvao pisca");
            errorMsg="Saving user failed";
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", errorMsg);
            throw new BpmnError("SAVE_USER_FAILED", "Saving user failed.");
        }

        execution.setVariable("currentUser", newWriter.getUsername());

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
}
