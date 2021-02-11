package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.BookRequest;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.services.interfaces.BookRequestService;
import team16.literaryassociation.services.interfaces.ManuscriptService;

@Service
public class SaveManuscriptService implements JavaDelegate {

    @Autowired
    private BookRequestService bookRequestService;
    @Autowired
    private ManuscriptService manuscriptService;
    @Value("${upload_folder}")
    private String uploadFolder;


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Usao u save manuscript service");
        Long bookRequestId = (Long) execution.getVariable("bookRequestId");
        String pdfManuscript = (String) execution.getVariable("pdfManuscript"); //putanja do fajla
        BookRequest br = this.bookRequestService.findById(bookRequestId);
        if(br == null){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", "Saving manuscript failed. Upload again.");
            throw new BpmnError("SAVING_MANUSCRIPT_FAILED");
        }
        Manuscript m = new Manuscript();
        m.setPdf(uploadFolder + execution.getProcessInstanceId() + "/" +pdfManuscript);
        m.setBookRequest(br);
        m.setOriginal(false);
        m.setAccepted(false);
        try {
            this.manuscriptService.save(m);
        }catch(Exception e){
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", "Saving manuscript failed. Upload again.");
            throw new BpmnError("SAVING_MANUSCRIPT_FAILED");
        }
        execution.setVariable("manuscriptId", m.getId());

        //ovde se jos odradi ono deponovanje u sistem za plagijarizam
        //postavi se promenljiva pdfDownloadPlagiarism koja ce zadrzati
        //url za download svih fajlova koji ce se moci download-ovati u
        //formatu downloadUrl1|downloadUrl2...
        //ovo je samo simulacija sada
        execution.setVariable("pdfDownloadPlagiarism", "https://localhost:8080/api/task/downloadFile?filePath=plagiarism-files/KontrolnaTacka.pdf|https://localhost:8080/api/task/downloadFile?filePath=plagiarism-files/KontrolnaTacka2.pdf");

    }
}
