package team16.literaryassociation.elasticsearch.service;

import team16.literaryassociation.elasticsearch.dto.SearchAdvancedDTO;
import team16.literaryassociation.elasticsearch.dto.SearchBasicDTO;
import team16.literaryassociation.elasticsearch.dto.SearchResultDTO;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    List<SearchResultDTO> basicSearch(SearchBasicDTO searchBasicDTO) throws IOException;
    List<SearchResultDTO> advancedSearch(List<SearchAdvancedDTO> searchAdvancedDTOS) throws IOException;
    List<BetaReaderIndexUnit> findBetaReadersForGenre(String genre, String writerUsername);
}
