package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.dto.GenreDTO;
import team16.literaryassociation.model.Genre;

import java.util.List;

public interface GenreService {

    List<GenreDTO> getAllGenres();
    Genre findById(Long id);
    Genre findByName(String name);
}
