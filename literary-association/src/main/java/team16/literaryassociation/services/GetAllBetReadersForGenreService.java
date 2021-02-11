package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.interfaces.ReaderService;

import java.util.HashMap;
import java.util.List;

@Service
public class GetAllBetReadersForGenreService implements JavaDelegate {

    @Autowired
    private ReaderService readerService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("USAO U GET BETA READERS SERVICE");
        // iz procesne varijable uzeti zanr i ponuditi beta-reader-a koji su zainteresovani za taj zanr
        String bookGenre = (String) delegateExecution.getVariable("genre");
        List<Reader> readers = readerService.getAllBetaReadersForGenre(bookGenre);
        int betaReadersNum = readers.size();
        delegateExecution.setVariable("betaReadersNumber", betaReadersNum);
    }
}
