package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.BoardOpinion;
import team16.literaryassociation.repository.BoardOpinionRepository;
import team16.literaryassociation.services.interfaces.BoardOpinionService;

@Service
public class BoardOpinionServiceImpl implements BoardOpinionService {

    @Autowired
    private BoardOpinionRepository boardOpinionRepository;

    @Override
    public BoardOpinion save(BoardOpinion boardOpinion) {
        return boardOpinionRepository.save(boardOpinion);
    }
}
