package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.repository.WriterRepository;
import team16.literaryassociation.services.interfaces.WriterService;

@Service
public class WriterServiceImpl implements WriterService {

    @Autowired
    private WriterRepository writerRepository;

    @Override
    public Writer saveWriter(Writer writer) {
        return writerRepository.save(writer);
    }

    @Override
    public Writer findByUsername(String username) {
        return this.writerRepository.findByUsername(username);
    }
}
