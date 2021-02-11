package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Manuscript;
import team16.literaryassociation.repository.ManuscriptRepository;
import team16.literaryassociation.services.interfaces.ManuscriptService;

@Service
public class ManuscriptServiceImpl implements ManuscriptService {

    @Autowired
    private ManuscriptRepository manuscriptRepository;

    @Override
    public Manuscript save(Manuscript manuscript) {
        return this.manuscriptRepository.save(manuscript);
    }

    @Override
    public Manuscript findById(Long id) {
        return this.manuscriptRepository.findById(id).orElse(null);
    }
}
