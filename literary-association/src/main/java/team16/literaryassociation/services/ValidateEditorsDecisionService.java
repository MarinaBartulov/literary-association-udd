package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidateEditorsDecisionService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Uslo u validate editor's decision service");
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);
        boolean decisionIsValid = true;

        boolean accept = (boolean )map.get("accept");
        if(!accept) {
            String reasonForRejection = (String) map.get("reasonForRejection");
            if (reasonForRejection.trim().equals("")) {
                decisionIsValid = false;
                execution.setVariable("globalError", true);
                execution.setVariable("globalErrorMessage", "Reason for rejection is required.");
            }
        }

        execution.setVariable("decisionIsValid", decisionIsValid);

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
