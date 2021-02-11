package team16.literaryassociation.listeners;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.EditorDTO;
import team16.literaryassociation.model.Editor;
import team16.literaryassociation.services.interfaces.EditorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GetNewEditorListener implements TaskListener {

    @Autowired
    private EditorService editorService;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("USAO U GetNewEditorListener");
        TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().
                getFormService().getTaskFormData(delegateTask.getId());
        List<Editor> editors = editorService.findAll();
        String mainEditor = (String) delegateTask.getExecution().getVariable("mainEditor");
        List<EditorDTO> editorDTOs = (List<EditorDTO>) delegateTask.getExecution().getVariable("chosenEditors");
        List<String> allTakenUsername = new ArrayList<>();
        for(EditorDTO ed: editorDTOs) {
            allTakenUsername.add(ed.getUsername());
        }
        allTakenUsername.add(mainEditor);
        List<Editor> remaining = new ArrayList<>();
        for(Editor e: editors) {
            if(!allTakenUsername.contains(e.getUsername())) {
                remaining.add(e);
            }
        }
        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields != null){
            for(FormField f: formFields){
                if(f.getId().equals("remainingEditors")){
                    HashMap<String, String> items = (HashMap<String, String>) f.getType().getInformation("values");
                    items.clear();
                    for(Editor e: remaining){
                        items.put(e.getId().toString(), e.getFirstName() + " " + e.getLastName());
                    }
                }
            }
        }
    }
}
