package team16.literaryassociation.listeners;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.stereotype.Service;
import team16.literaryassociation.enums.Opinion;

import java.util.HashMap;
import java.util.List;

@Service
public class GetAllOpinionsListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("USAO U GET OPINIONS LISTENER");
        TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().
                getFormService().getTaskFormData(delegateTask.getId());
        Opinion[] opinions = Opinion.values();
        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields != null){
            for(FormField f: formFields){
                if(f.getId().equals("opinion") ){
                    HashMap<String, String> items = (HashMap<String, String>) f.getType().getInformation("values");
                    items.clear();
                    for(Opinion opinion : opinions){
                        //items.put(String.valueOf(opinion.ordinal()),opinion.name());
                        items.put(opinion.name(),opinion.name());
                    }
                }
            }
        }
    }
}
