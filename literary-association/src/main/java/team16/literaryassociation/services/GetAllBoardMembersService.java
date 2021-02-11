package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.BoardMemberDTO;
import team16.literaryassociation.services.interfaces.BoardMemberService;

import java.util.List;

@Service
public class GetAllBoardMembersService  implements JavaDelegate {

    @Autowired
    private BoardMemberService boardMemberService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Usao u GetAllBoardMembers Service");
        List<BoardMemberDTO> boardMembersDTO = boardMemberService.getAllBoardMembersDTO();

        execution.setVariable("boardMembers", boardMembersDTO);
        execution.setVariable("boardMembersNumber", boardMembersDTO.size());
        execution.setVariable("cycleNumber", 0);
    }

}
