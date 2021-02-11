package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Lecturer;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.services.interfaces.LecturerService;
import team16.literaryassociation.services.interfaces.ManuscriptService;

@Service
public class ChooseRandomLecturerService implements JavaDelegate {

    @Autowired
    private ManuscriptService manuscriptService;

    @Autowired
    private LecturerService lecturerService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("Usao u ChooseRandomLecturerService");

        Long manuscriptId = (Long) delegateExecution.getVariable("manuscriptId");
        Manuscript manuscript = manuscriptService.findById(manuscriptId);
        if(manuscript == null) {
            System.out.println("Nije nasao Manuscript");
            throw new BpmnError("LECTURER_CHOOSING_FAILED", "Invalid manuscript.");
        }

        Lecturer lecturer = lecturerService.findRandomEditor();
        manuscript.setLecturer(lecturer);
        Manuscript saved = null;
        try {
            saved = manuscriptService.save(manuscript);
        } catch (Exception e) {
            throw new BpmnError("LECTURER_CHOOSING_FAILED", "Saving manuscript failed.");
        }

        delegateExecution.setVariable("lecturer", saved.getLecturer().getUsername());
    }
}
