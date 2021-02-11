package team16.literaryassociation.services;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.FormSubmissionDTO;
import team16.literaryassociation.enums.Opinion;
import team16.literaryassociation.model.*;
import team16.literaryassociation.services.interfaces.BoardOpinionService;
import team16.literaryassociation.services.interfaces.CommentService;
import team16.literaryassociation.services.interfaces.MembershipApplicationService;
import team16.literaryassociation.services.interfaces.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveOpinionAndCommentService implements JavaDelegate {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private BoardOpinionService boardOpinionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipApplicationService membershipApplicationService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u save opinion and comment");
        String errorMsg = "";
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, Object> map = this.listFieldsToMap(formData);

        Long membershipApplicationId = (Long) execution.getVariable("membership_application_id");
        MembershipApplication membershipApplication = membershipApplicationService.findById(membershipApplicationId);
        if(membershipApplication == null)
        {
            errorMsg = "Error saving opinion and comment, membership application not found";
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", errorMsg);
            throw new BpmnError("SAVE_OPINION_AND_COMMENT_FAILED", "Membership application not found.");
        }

        String username = this.identityService.getCurrentAuthentication().getUserId();
        BoardMember boardMember = (BoardMember) userService.findByUsername(username);
        if(boardMember == null)
        {
            System.out.println("Nije nasao BoardMembera");
            errorMsg = "Error saving opinion and comment, board member not found";
            execution.setVariable("globalError", true);
            execution.setVariable("globalErrorMessage", errorMsg);
            throw new BpmnError("SAVE_OPINION_AND_COMMENT_FAILED", "Board member not found.");
        }

        BoardOpinion boardOpinion = new BoardOpinion();
        String opinion = (String) map.get("opinion");
        System.out.println("Vrednost enuma: " + opinion);
        boardOpinion.setOpinion(Opinion.valueOf(opinion));
        boardOpinion.setMembershipApplication(membershipApplication);
        boardOpinion.setBoardMember(boardMember);

        Comment comment = new Comment();
        comment.setContent((String) map.get("comment"));
        comment.setMembershipApplication(membershipApplication);
        comment.setBoardMember(boardMember);

        boardOpinionService.save(boardOpinion);
        commentService.save(comment);

        if(boardOpinion.getOpinion().equals(Opinion.NOT_ELEGIBLE))
        {
            Integer counter = (Integer)execution.getVariable("negativeOpinionCounter");
            counter++;
            execution.setVariable("negativeOpinionCounter", counter);
        }
        else if (boardOpinion.getOpinion().equals(Opinion.MORE_MATERIAL_NEEDED))
        {
            Integer counter = (Integer)execution.getVariable("moreMaterialNeeded");
            counter++;
            execution.setVariable("moreMaterialNeeded", counter);
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
