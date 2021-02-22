package team16.literaryassociation.listeners;

import lombok.SneakyThrows;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.elasticsearch.dto.BRSearchResultDTO;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;
import team16.literaryassociation.elasticsearch.service.SearchService;

import java.util.HashMap;
import java.util.List;

@Service
public class GetAllBetaReadersForGenreListener implements TaskListener {


    @Autowired
    private SearchService searchService;

    @SneakyThrows
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("USAO U GET BETA READERS LISTENER");
        TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().
                getFormService().getTaskFormData(delegateTask.getId());
        String bookGenre = (String) delegateTask.getExecution().getVariable("genre");
        String writerUsername = (String) delegateTask.getExecution().getVariable("writer");
        List<BRSearchResultDTO> betaReaders = this.searchService.findBetaReadersForGenre(bookGenre, writerUsername);

        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields != null){
            for(FormField f: formFields){
                if(f.getId().equals("betaReaders")){
                    HashMap<String, String> items = (HashMap<String, String>) f.getType().getInformation("values");
                    items.clear();
                    for(BRSearchResultDTO br: betaReaders){
                        items.put(br.getBetaReaderId().toString(), br.getFullName());
                    }
                }
            }
        }
    }
}
