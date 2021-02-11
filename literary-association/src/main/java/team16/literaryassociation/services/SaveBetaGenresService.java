package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.interfaces.GenreService;
import team16.literaryassociation.services.interfaces.ReaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveBetaGenresService implements JavaDelegate {

    @Autowired
    private ReaderService readerService;
    @Autowired
    private GenreService genreService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Long readerId = (Long) execution.getVariable("readerId");

        Reader reader = this.readerService.findById(readerId);
        if(reader == null){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", "Saving genres for beta reader failed.");
            throw new BpmnError("SAVING_BETA_GENRES_FAILED");
        }
        List<String> betaGenres = (List<String>) map.get("betaGenres");

        for(String genreName : betaGenres){
            Genre genre = this.genreService.findByName(genreName);
            reader.getBetaGenres().add(genre);
        }

        try{
            this.readerService.saveReader(reader);
        }catch(Exception e){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", "Saving genres for beta reader failed.");
            throw new BpmnError("SAVING_BETA_GENRES_FAILED");
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
