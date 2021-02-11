package team16.literaryassociation.services;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;

public class MembershipFeePayment implements JavaDelegate {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u membership fee payment send task");

        execution.getProcessEngineServices()
                .getRuntimeService()
                .createMessageCorrelation("MembershipFeePaid")
                .correlate();

    }

}
