package team16.literaryassociation.elasticsearch.service;

import org.springframework.web.multipart.MultipartFile;
import team16.literaryassociation.elasticsearch.dto.BRSearchResultDTO;
import team16.literaryassociation.elasticsearch.dto.SearchAdvancedDTO;
import team16.literaryassociation.elasticsearch.dto.SearchBasicDTO;
import team16.literaryassociation.elasticsearch.dto.SearchResultDTO;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SearchService {

    List<SearchResultDTO> basicSearch(SearchBasicDTO searchBasicDTO) throws IOException;
    List<SearchResultDTO> advancedSearch(List<SearchAdvancedDTO> searchAdvancedDTOS) throws IOException;
    List<BRSearchResultDTO> findBetaReadersForGenre(String genre, String writerUsername) throws IOException;
    String checkPlagiarism(MultipartFile file) throws IOException;
}
