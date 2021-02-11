package team16.literaryassociation.services;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.services.interfaces.ManuscriptService;

@Service
public class SaveUpdatedManuscriptService implements JavaDelegate {

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private RuntimeService runtimeService;

    @Value("${upload_folder}")
    private String uploadFolder;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("Usao u SaveUpdatedManuscriptService");

        Long manuscriptId = (Long) delegateExecution.getVariable("manuscriptId");
        Manuscript manuscript = manuscriptService.findById(manuscriptId);
        if(manuscript == null)
        {
            System.out.println("Nije nasao Manuscript");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Manuscript cannot be found.");
            throw new BpmnError("MANUSCRIPT_SAVING_FAILED", "Saving manuscript failed.");
        }

        String pdfManuscript = (String) delegateExecution.getVariable("pdfManuscript"); //putanja do fajla
        manuscript.setPdf(uploadFolder + delegateExecution.getProcessInstanceId() + "/" + pdfManuscript);
        try {
            manuscriptService.save(manuscript);
        } catch(Exception e) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Saving manuscript failed.");
            throw new BpmnError("MANUSCRIPT_SAVING_FAILED", "Saving manuscript failed.");
        }
    }
}
