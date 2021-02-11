package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.model.BoardMember;
import team16.literaryassociation.model.Comment;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.interfaces.CommentService;
import team16.literaryassociation.services.interfaces.ManuscriptService;
import team16.literaryassociation.services.interfaces.ReaderService;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveBetaReaderCommentService implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ManuscriptService manuscriptService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Usao u save beta reader comment service");

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) delegateExecution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Long manuscriptId = (Long) delegateExecution.getVariable("manuscriptId");
        Manuscript manuscript = manuscriptService.findById(manuscriptId);
        if(manuscript == null)
        {
            System.out.println("Nije nasao Manuscript");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Manuscript cannot be found.");
            throw new BpmnError("COMMENT_SAVING_FAILED", "Saving comment failed.");
        }

        String username = this.identityService.getCurrentAuthentication().getUserId();
        Reader betaReader = (Reader) userService.findByUsername(username);
        if(betaReader == null)
        {
            System.out.println("Nije nasao Reader-a");
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Beta-Reader cannot be found.");
            throw new BpmnError("COMMENT_SAVING_FAILED", "Saving comment failed.");
        }

        Comment comment = new Comment();
        comment.setContent((String) map.get("comment"));
        comment.setBetaReader(betaReader);
        comment.setManuscript(manuscript);
        try {
            commentService.save(comment);
        } catch (Exception e) {
            delegateExecution.setVariable("globalError", true);
            delegateExecution.setVariable("globalErrorMessage", "Saving comment failed.");
            throw new BpmnError("COMMENT_SAVING_FAILED", "Saving comment failed.");
        }

    }

    private Map<String, Object> listFieldsToMap(List<FormSubmissionDTO> formData) {
        Map<String, Object> retVal = new HashMap<>();
        for(FormSubmissionDTO dto: formData) {
            if(dto.getFieldId() != null) {
                retVal.put(dto.getFieldId(), dto.getFieldValue());
            }
        }
        return retVal;
    }
}
