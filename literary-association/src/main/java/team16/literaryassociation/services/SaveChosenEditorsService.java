package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.BetaReaderDTO;
import team16.literaryassociation.dto.EditorDTO;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.mapper.BetaReaderMapper;
import team16.literaryassociation.mapper.EditorMapper;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.model.PlagiarismComplaint;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.EditorService;
import team16.literaryassociation.services.interfaces.PlagiarismComplaintService;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaveChosenEditorsService implements JavaDelegate {

    @Autowired
    private PlagiarismComplaintService plagiarismComplaintService;

    @Autowired
    private EditorService editorService;

    @Autowired
    private BookService bookService;

    @Autowired
    private EditorMapper editorMapper = new EditorMapper();

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u save chosen EDITORS");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) delegateExecution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Long plagiarismComplaintId = (Long) delegateExecution.getVariable("plagiarismComplaintId");
        PlagiarismComplaint plagiarismComplaint = plagiarismComplaintService.findById(plagiarismComplaintId);
        if(plagiarismComplaint == null)
        {
            System.out.println("Nije nasao PlagiarismComplaint");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Plagiarism complaint could not be found.");
            throw new BpmnError("SAVING_CHOSEN_EDITORS_FAILED", "Saving chosen editors failed.");
        }

        Book myBook = plagiarismComplaint.getMyBook();

        System.out.println("PRE BRISANJA EDITORA: " + myBook.getOtherEditors().size());
        myBook.getOtherEditors().clear();
        System.out.println("POSLE BRISANJA EDITORA: " + myBook.getOtherEditors().size());

        List<String> editors = (List<String>) map.get("editors");
        for (String editor : editors) {
            Editor e = editorService.findById(Long.parseLong(editor));
            if(e == null) {
                delegateExecution.setVariable("globalError", true);
                delegateExecution.setVariable("globalErrorMessage", "Chosen editor could not be found.");
                throw new BpmnError("SAVING_CHOSEN_EDITORS_FAILED", "Saving chosen editors failed.");
            }
            myBook.getOtherEditors().add(e);
        }

        try {
            Book saved = bookService.save(myBook);
            List<EditorDTO> editorDTOs = saved.getOtherEditors().stream().map(e -> editorMapper.toDto(e)).collect(Collectors.toList());
            delegateExecution.setVariable("chosenEditors", editorDTOs);
        } catch (Exception e) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Saving chosen editors to plagiarism complaint failed.");
            throw new BpmnError("SAVING_CHOSEN_EDITORS_FAILED", "Saving chosen editors failed.");
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
