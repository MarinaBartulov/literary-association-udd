package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.BoardMemberDTO;
import team16.literaryassociation.mapper.BoardMemberMapper;
import team16.literaryassociation.model.BoardMember;
import team16.literaryassociation.repository.BoardMemberRepository;
import team16.literaryassociation.services.interfaces.BoardMemberService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardMemberServiceImpl implements BoardMemberService {

    @Autowired
    private BoardMemberRepository boardMemberRepository;

    @Autowired
    private BoardMemberMapper boardMemberMapper = new BoardMemberMapper();


    @Override
    public List<BoardMemberDTO> getAllBoardMembersDTO() {
        return boardMemberRepository.findAll().stream().map( bm -> boardMemberMapper.toDto(bm)).collect(Collectors.toList());
    }

    @Override
    public List<BoardMember> findAll() {
        return boardMemberRepository.findAll();
    }

    @Override
    public BoardMember getOne(Long id) {
        return boardMemberRepository.getOne(id);
    }


}
