package team16.literaryassociation.listeners;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.MerchantDTO;
import team16.literaryassociation.services.interfaces.MerchantService;

import java.util.HashMap;
import java.util.List;

@Service
public class GetAllActiveMerchants implements TaskListener {

    @Autowired
    private MerchantService merchantService;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("USAO U GetAllActiveMerchants");
        TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().
                getFormService().getTaskFormData(delegateTask.getId());
        List<MerchantDTO> merchants = merchantService.findAllActiveMerchants();
        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields != null){
            for(FormField f: formFields){
                if(f.getId().equals("publisher")){
                    HashMap<String, String> items = (HashMap<String, String>) f.getType().getInformation("values");
                    items.clear();
                    for(MerchantDTO m: merchants){
                        items.put(m.getEmail(), m.getName());
                    }
                }
            }
        }
    }
}
