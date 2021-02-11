package team16.literaryassociation.listeners;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.services.interfaces.EditorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GetAllOtherEditorsListener implements TaskListener {

    @Autowired
    private EditorService editorService;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("USAO U GET all other Editors");
        TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().
                getFormService().getTaskFormData(delegateTask.getId());
        List<Editor> editors = editorService.findAll();
        String mainEditor = (String) delegateTask.getExecution().getVariable("mainEditor");
        List<Editor> notMain = new ArrayList<>();
        for(Editor e: editors) {
            if(!e.getUsername().equals(mainEditor)) {
                notMain.add(e);
            }
        }
        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields != null){
            for(FormField f: formFields){
                if(f.getId().equals("editors")){
                    HashMap<String, String> items = (HashMap<String, String>) f.getType().getInformation("values");
                    items.clear();
                    for(Editor e: notMain){
                        items.put(e.getId().toString(), e.getFirstName() + " " + e.getLastName());
                    }
                }
            }
        }
    }
}
