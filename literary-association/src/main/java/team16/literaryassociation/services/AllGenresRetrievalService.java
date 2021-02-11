package team16.literaryassociation.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.GenreDTO;
import team16.literaryassociation.mapper.GenreMapper;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllGenresRetrievalService implements JavaDelegate {

    @Autowired
    private GenreRepository genreRepository;

    private GenreMapper genreMapper;

    public AllGenresRetrievalService() {
        genreMapper = new GenreMapper();
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<Genre> allGenres = genreRepository.findAll();

        List<GenreDTO> genresDTO = createGenreDTOList(allGenres);
        delegateExecution.setVariable("genres", genresDTO);

        System.out.println(delegateExecution.getVariable("genres"));
    }

    private List<GenreDTO> createGenreDTOList(List<Genre> entities) {
        return entities.stream().map(entity -> genreMapper.toDto(entity)).collect(Collectors.toList());
    }
}
