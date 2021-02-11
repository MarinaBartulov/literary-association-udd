package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.LiteraryWork;
import team16.literaryassociation.model.MembershipApplication;
import team16.literaryassociation.services.interfaces.LiteraryWorkService;
import team16.literaryassociation.services.interfaces.MembershipApplicationService;


@Service
public class SaveLiteraryWorkService implements JavaDelegate {

    @Autowired
    private MembershipApplicationService membershipApplicationService;

    @Autowired
    private LiteraryWorkService literaryWorkService;

    @Value("${upload_folder}")
    private String uploadFolder;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u save literary work service");
        String errorMsg = "";

        execution.setVariable("negativeOpinionCounter", 0); // daju misljenje ispocetka
        execution.setVariable("moreMaterialNeeded", 0); // daju misljenje ispocetka
        Long membershipApplicationId = (Long) execution.getVariable("membership_application_id");
        System.out.println("membership application id: " + membershipApplicationId);

        MembershipApplication membershipApplication = membershipApplicationService.findById(membershipApplicationId);
        if(membershipApplication == null)
        {
            System.out.println("Nije nasao MembershipApplication");
            errorMsg = "Error saving literary work, membership application not found";
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", errorMsg);
            throw new BpmnError("SAVE_LITERARY_WORK_FAILED", "Membership application not found.");
        }

        String pdfs = (String) execution.getVariable("pdf");
        String[] titles = pdfs.split("\\|");

        for(String title : titles)
        {
            System.out.println("Pravi Literary Work");
            System.out.println(title);
            String path = uploadFolder + execution.getProcessInstanceId() + "/" + title;
            LiteraryWork literaryWork = new LiteraryWork(title,path);
            literaryWork.setMembershipApplication(membershipApplication);
            try{
                literaryWorkService.save(literaryWork);
            }catch (Exception e){
                errorMsg = "Error saving literary work";
                execution.setVariable("globalError", true);
                execution.setVariable("globalErrorMessage", errorMsg);
                throw new BpmnError("SAVE_LITERARY_WORK_FAILED", "Literary work not created.");
            }
        }

        //List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        //System.out.println("Velicina formData: "+ formData.size() );

        /*int i = 0;
        for(FormSubmissionDTO dto : formData) {
            System.out.println("Pravi Literary Work, brojac:" + i);
            String title = (String) dto.getFieldValue();
            String path = (String) execution.getVariable("pdfFileLocation" + i);
            String url = (String) execution.getVariable("url" + i);

            LiteraryWork literaryWork = new LiteraryWork(title, path, url);
            literaryWork.setMembershipApplication(membershipApplication);
            LiteraryWork literaryWorkSaved = literaryWorkService.save(literaryWork);
            if (literaryWorkSaved == null)
            {
                throw new BpmnError("SAVE_LITERARY_WORK_FAILED", "Literary work not created.");
            }
            i++;
        }*/
    }
}
