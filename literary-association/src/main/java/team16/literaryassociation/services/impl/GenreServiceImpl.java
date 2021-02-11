package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.GenreDTO;
import team16.literaryassociation.mapper.GenreMapper;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.repository.GenreRepository;
import team16.literaryassociation.services.interfaces.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;

    @Override
    public List<GenreDTO> getAllGenres() {
        return this.genreRepository.findAll().stream().map(g -> genreMapper.toDto(g)).collect(Collectors.toList());
    }

    @Override
    public Genre findById(Long id) {
        return this.genreRepository.findById(id).orElse(null);
    }

    @Override
    public Genre findByName(String name) {
        return this.genreRepository.findByName(name);
    }
}
