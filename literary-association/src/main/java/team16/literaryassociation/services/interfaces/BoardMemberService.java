package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.dto.BoardMemberDTO;
import team16.literaryassociation.model.BoardMember;

import java.util.List;

public interface BoardMemberService {

    List<BoardMemberDTO> getAllBoardMembersDTO();
    List<BoardMember> findAll();
    BoardMember getOne(Long id);
}
