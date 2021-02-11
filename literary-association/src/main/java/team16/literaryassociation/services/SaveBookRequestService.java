package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.BookRequest;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.BookRequestService;
import team16.literaryassociation.services.interfaces.GenreService;
import team16.literaryassociation.services.interfaces.WriterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveBookRequestService implements JavaDelegate {

    @Autowired
    private GenreService genreService;
    @Autowired
    private BookRequestService bookRequestService;
    @Autowired
    private WriterService writerService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);
        BookRequest br = new BookRequest();
        br.setTitle((String)map.get("title"));
        br.setSynopsis((String) map.get("synopsis"));
        String genreName = (String) map.get("genre");
        Genre genre = this.genreService.findByName(genreName);
        br.setGenre(genre);
        String username = (String) execution.getVariable("writer");
        Writer writer  = this.writerService.findByUsername(username);
        if(writer == null){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", "Writer can't be found. Saving book request failed.");
            throw new BpmnError("SAVING_BOOK_REQUEST_FAILED");
        }
        br.setWriter(writer);
        try {
            this.bookRequestService.save(br);
            execution.setVariable("bookRequestId", br.getId());
        }catch(Exception e){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", "Saving book request failed.");
            throw new BpmnError("SAVING_BOOK_REQUEST_FAILED");
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
