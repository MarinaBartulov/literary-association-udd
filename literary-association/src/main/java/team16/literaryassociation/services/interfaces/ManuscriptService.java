package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.Manuscript;

public interface ManuscriptService {

    Manuscript save(Manuscript manuscript);
    Manuscript findById(Long id);
}
