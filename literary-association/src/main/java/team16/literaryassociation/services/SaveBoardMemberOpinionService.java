package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveBoardMemberOpinionService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u SaveBoardMemberOpinionService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Boolean opinion = (Boolean) map.get("isPlagiat");
        System.out.println("OPINION: " + opinion);
        if(opinion)
        {
            Integer plagiat = (Integer) execution.getVariable("plagiat");
            plagiat ++;
            execution.setVariable("plagiat", plagiat);
        }
        else
        {
            Integer notPlagiat = (Integer) execution.getVariable("notPlagiat");
            notPlagiat ++;
            execution.setVariable("notPlagiat", notPlagiat);
        }

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
