package team16.literaryassociation.listeners;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.model.User;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.HashMap;
import java.util.List;

@Service
public class GetAllWritersBooksListener implements TaskListener {

    @Autowired
    private BookService bookService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("USAO U GET ALL WRITERS BOOKS LISTENER");
        TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().
                getFormService().getTaskFormData(delegateTask.getId());
        String username = this.identityService.getCurrentAuthentication().getUserId();
        User user = userService.findByUsername(username);
        List<Book> books = bookService.findAllWritersBooks(user.getId());
        List<FormField> formFields = taskFormData.getFormFields();
        if(formFields != null){
            for(FormField f: formFields){
                if(f.getId().equals("myBookTitle")){
                    HashMap<String, String> items = (HashMap<String, String>) f.getType().getInformation("values");
                    items.clear();
                    for(Book b: books){
                        items.put(b.getId().toString(), b.getTitle());
                    }
                }
            }
        }
    }
}
