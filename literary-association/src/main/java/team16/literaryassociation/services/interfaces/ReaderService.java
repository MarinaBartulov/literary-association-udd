package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.Reader;

import java.util.List;

public interface ReaderService {

    Reader findById(Long id);
    Reader saveReader(Reader reader);
    List<Reader> getAllBetaReaders();
    List<Reader> getAllBetaReadersForGenre(String name);
    Reader findByUsername(String username);
}
