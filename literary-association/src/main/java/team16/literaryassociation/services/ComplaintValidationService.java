package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.model.User;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComplaintValidationService implements JavaDelegate {

    @Autowired
    private BookService bookService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Uslo u ComplaintValidationService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) delegateExecution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);
        boolean complaintIsValid = true;
        String errorMsg = "";

        String myBookTitleId = (String) map.get("myBookTitle");
        if(bookService.findOne(Long.parseLong(myBookTitleId)) == null){
            complaintIsValid = false;
            errorMsg += "Chosen book doesn't exist. ";
        }

        if(((String)map.get("plagiatBookTitle")).equals("")){
            complaintIsValid = false;
            errorMsg += "Plagiarism book title is required. ";
        }
        if(((String)map.get("writerFirstName")).equals("")){
            complaintIsValid = false;
            errorMsg += "Plagiarism book writer's first name is required. ";
        }
        if(((String)map.get("writerLastName")).equals("")){
            complaintIsValid = false;
            errorMsg += "Plagiarism book writer's last name is required. ";
        }

        if(!complaintIsValid){
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", errorMsg);
        }

        delegateExecution.setVariable("myBookId", Long.parseLong(myBookTitleId));
        delegateExecution.setVariable("complaintIsValid", complaintIsValid);
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
