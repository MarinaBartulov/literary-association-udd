package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.BookRequest;

public interface BookRequestService {

    BookRequest save(BookRequest bookRequest);
    BookRequest findById(Long id);
}
