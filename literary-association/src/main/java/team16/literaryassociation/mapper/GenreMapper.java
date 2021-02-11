package team16.literaryassociation.mapper;

import org.springframework.stereotype.Component;
import team16.literaryassociation.dto.GenreDTO;
import team16.literaryassociation.model.Genre;

@Component
public class GenreMapper implements IMapper<Genre, GenreDTO> {

    @Override
    public Genre toEntity(GenreDTO dto) {
        return new Genre(dto.getName(), dto.getDescription());
    }

    @Override
    public GenreDTO toDto(Genre entity) {
        return new GenreDTO(entity.getId(), entity.getName(), entity.getDescription());
    }
}
