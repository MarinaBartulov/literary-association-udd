package team16.literaryassociation.mapper;

import org.springframework.stereotype.Component;
import team16.literaryassociation.dto.BoardMemberDTO;
import team16.literaryassociation.model.BoardMember;

@Component
public class BoardMemberMapper implements IMapper<BoardMember, BoardMemberDTO> {

    @Override
    public BoardMember toEntity(BoardMemberDTO dto) {
        return new BoardMember(dto);
    }

    @Override
    public BoardMemberDTO toDto(BoardMember entity)
    {
        return new BoardMemberDTO(entity);
    }
}
