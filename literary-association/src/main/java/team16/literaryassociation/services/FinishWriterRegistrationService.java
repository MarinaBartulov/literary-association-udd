package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.MembershipApplication;
import team16.literaryassociation.services.interfaces.MembershipApplicationService;

@Service
public class FinishWriterRegistrationService implements JavaDelegate {

    @Autowired
    private MembershipApplicationService membershipApplicationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Usao u Finish Writer Registration");

        Long id = (Long)execution.getVariable("membership_application_id");
        MembershipApplication membershipApplication = membershipApplicationService.findById(id);
        if(membershipApplication == null)
        {
            throw new BpmnError("WRITER_REGISTRATION_FINISH_FAILED", "Writer registration failed, Membership application not found.");
        }
        membershipApplication.setApproved(true);
        membershipApplication.setPaid(true);
        membershipApplicationService.save(membershipApplication);

    }
}
