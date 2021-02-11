package team16.literaryassociation.listeners;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.interfaces.ReaderService;

import java.util.HashMap;
import java.util.List;

@Service
public class GetAllBetaReadersForGenreListener implements TaskListener {

    @Autowired
    private ReaderService readerService;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("USAO U GET BETA READERS LISTENER");
        TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().
                getFormService().getTaskFormData(delegateTask.getId());
        // iz procesne varijable uzeti zanr i ponuditi beta-reader-a koji su zainteresovani za taj zanr
        String bookGenre = (String) delegateTask.getExecution().getVariable("genre");
        List<Reader> readers = readerService.getAllBetaReadersForGenre(bookGenre);
        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields != null){
            for(FormField f: formFields){
                if(f.getId().equals("betaReaders")){
                    HashMap<String, String> items = (HashMap<String, String>) f.getType().getInformation("values");
                    items.clear();
                    for(Reader r: readers){
                        items.put(r.getId().toString(), r.getFirstName() + " " + r.getLastName());
                    }
                }
            }
        }
    }
}
