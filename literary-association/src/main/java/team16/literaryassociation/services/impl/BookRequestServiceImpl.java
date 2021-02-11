package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.BookRequest;
import team16.literaryassociation.repository.BookRequestRepository;
import team16.literaryassociation.services.interfaces.BookRequestService;

@Service
public class BookRequestServiceImpl implements BookRequestService {

    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Override
    public BookRequest save(BookRequest bookRequest) {
        return this.bookRequestRepository.save(bookRequest);
    }

    @Override
    public BookRequest findById(Long id) {
        return this.bookRequestRepository.findById(id).orElse(null);
    }


}
