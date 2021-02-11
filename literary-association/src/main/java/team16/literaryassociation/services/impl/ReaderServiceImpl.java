package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.repository.ReaderRepository;
import team16.literaryassociation.services.interfaces.ReaderService;

import java.util.List;

@Service
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    public Reader findById(Long id) {
        return readerRepository.findById(id).orElseGet(null);
    }

    @Override
    public Reader saveReader(Reader reader) {
        return this.readerRepository.save(reader);
    }

    @Override
    public List<Reader> getAllBetaReaders() {
        return readerRepository.findAllBetReaders();
    }

    @Override
    public List<Reader> getAllBetaReadersForGenre(String name) {
        return readerRepository.findAllBetReadersForGenre(name);
    }

    @Override
    public Reader findByUsername(String username) {
        return this.readerRepository.findByUsername(username);
    }
}
