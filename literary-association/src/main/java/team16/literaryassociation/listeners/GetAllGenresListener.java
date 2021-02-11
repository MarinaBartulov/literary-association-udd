package team16.literaryassociation.listeners;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.repository.GenreRepository;

import java.util.HashMap;
import java.util.List;

@Service
public class GetAllGenresListener implements TaskListener {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().
                getFormService().getTaskFormData(delegateTask.getId());
        List<Genre> allGenres = this.genreRepository.findAll();
        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields != null){
            for(FormField f: formFields){
                if(f.getId().equals("genres") || f.getId().equals("betaGenres") || f.getId().equals("genre")){
                    HashMap<String, String> items = (HashMap<String, String>) f.getType().getInformation("values");
                    items.clear();
                    for(Genre g: allGenres){
                        items.put(g.getName(),g.getName());
                    }
                }
            }
        }
    }
}
