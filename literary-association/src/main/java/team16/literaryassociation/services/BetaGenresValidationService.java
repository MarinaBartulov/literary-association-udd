package team16.literaryassociation.services;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.services.interfaces.GenreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BetaGenresValidationService implements JavaDelegate {

    @Autowired
    private GenreService genreService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);
        boolean isValid = true;
        String errorMsg = "";

        List<String> betaGenres = (List<String>) map.get("betaGenres");
        if(betaGenres.size() < 1){
            isValid = false;
            errorMsg += "At least one genre is required.";

        }
        for(String genre : betaGenres){
            if(this.genreService.findByName(genre) == null){
                isValid = false;
                errorMsg += "Genre with name " + genre + " doesn't exist";
                break;
            }
        }

        if(!isValid){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", errorMsg);
        }

        execution.setVariable("betaGenresValid", isValid);
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
