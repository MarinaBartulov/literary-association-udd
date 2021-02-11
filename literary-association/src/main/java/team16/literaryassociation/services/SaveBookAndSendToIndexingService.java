package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.*;
import team16.literaryassociation.services.interfaces.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveBookAndSendToIndexingService implements JavaDelegate {

    @Autowired
    private GenreService genreService;

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private BookService bookService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u SaveBookAndSendToIndexingService");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) delegateExecution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Book book = new Book();

        book.setPlagiarism(false);
        book.setTitle((String) map.get("title"));
        book.setSynopsis((String) map.get("synopsis"));
        book.setISBN((String) map.get("ISBN"));
        book.setYear((String) map.get("year"));
        try {
            book.setNumOfPages(Integer.parseInt((String) map.get("numberOfPages")));
        } catch (Exception e) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Invalid number of pages.");
            throw new BpmnError("BOOK_SAVING_FAILED", "Saving book failed.");
        }

        String genreName = (String) map.get("genre");
        Genre genre = this.genreService.findByName(genreName);
        if (genre == null) {
            System.out.println("Nije nasao Genre");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Book genre cannot be found.");
            throw new BpmnError("BOOK_SAVING_FAILED", "Saving book failed.");
        }

        Long manuscriptId = (Long) delegateExecution.getVariable("manuscriptId");
        Manuscript manuscript = manuscriptService.findById(manuscriptId);
        if (manuscript == null) {
            System.out.println("Nije nasao manuscript");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Manuscript cannot be found.");
            throw new BpmnError("BOOK_SAVING_FAILED", "Saving book failed.");
        }

        book.setGenre(genre);
        book.setManuscript(manuscript);
        book.setPdf(manuscript.getPdf());

        String writerUsername = (String) delegateExecution.getVariable("writer");
        Writer writer = (Writer) userService.findByUsername(writerUsername);
        if (writer == null) {
            System.out.println("Nije nasao writer");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Writer cannot be found.");
            throw new BpmnError("BOOK_SAVING_FAILED", "Saving book failed.");
        }
        book.setWriter(writer);

        String editorUsername = (String) delegateExecution.getVariable("chosenEditor");
        Editor editor = (Editor) userService.findByUsername(editorUsername);
        if (editor == null) {
            System.out.println("Nije nasao editor");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Editor cannot be found.");
            throw new BpmnError("BOOK_SAVING_FAILED", "Saving book failed.");
        }
        book.setEditor(editor);

        String lecturerUsername = (String) delegateExecution.getVariable("lecturer");
        Lecturer lecturer = (Lecturer) userService.findByUsername(lecturerUsername);
        if (lecturer == null) {
            System.out.println("Nije nasao lecturer");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Lecturer cannot be found.");
            throw new BpmnError("BOOK_SAVING_FAILED", "Saving book failed.");
        }
        book.setLecturer(lecturer);

        String publisher = (String) map.get("publisher");
        Merchant merchant = merchantService.findByEmail(publisher);
        if(merchant == null) {
            System.out.println("Nije nasao merchant");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Publisher cannot be found.");
            throw new BpmnError("BOOK_SAVING_FAILED", "Saving book failed.");
        }
        book.setPublisher(merchant);
        book.setPublishersAddress("Trg Nikole Pasica 4, Beograd");

        try {
            bookService.save(book);
        } catch (Exception e) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Saving book failed.");
            throw new BpmnError("BOOK_SAVING_FAILED", "Saving book failed.");
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
