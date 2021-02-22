package team16.literaryassociation.elasticsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.elasticsearch.dto.BRSearchResultDTO;
import team16.literaryassociation.elasticsearch.dto.SearchAdvancedDTO;
import team16.literaryassociation.elasticsearch.dto.SearchBasicDTO;
import team16.literaryassociation.elasticsearch.dto.SearchResultDTO;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;
import team16.literaryassociation.elasticsearch.service.SearchService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;



    @PostMapping(value = "/basic")
    public ResponseEntity basicSearch(@RequestBody SearchBasicDTO searchBasicDTO) throws IOException {

        if(!searchBasicDTO.getField().equals("title") && !searchBasicDTO.getField().equals("writer")
        && !searchBasicDTO.getField().equals("content") && !searchBasicDTO.getField().equals("genre")
        && !searchBasicDTO.getField().equals("all")){
            return ResponseEntity.badRequest().body("You can't search by field " + searchBasicDTO.getField());
        }
        if(searchBasicDTO.getQuery().trim().equals("")){
            return ResponseEntity.badRequest().body("Query can't be empty.");
        }

        List<SearchResultDTO> searchResultDTOS = this.searchService.basicSearch(searchBasicDTO);
        return new ResponseEntity(searchResultDTOS, HttpStatus.OK);
    }

    @PostMapping(value = "/advanced")
    public ResponseEntity advancedSearch(@RequestBody List<SearchAdvancedDTO> searchAdvancedDTOS) throws IOException {

        if(searchAdvancedDTOS.size() < 2){
            return ResponseEntity.badRequest().body("You have to add at least 2 conditions for search");
        }
        for(SearchAdvancedDTO dto : searchAdvancedDTOS){
            if(!dto.getField().equals("title") && !dto.getField().equals("writer")
                    && !dto.getField().equals("content") && !dto.getField().equals("genre")
                    && !dto.getField().equals("all")){
                return ResponseEntity.badRequest().body("You can't search by field " + dto.getField());
            }
            if(dto.getQuery().trim().equals("")){
                return ResponseEntity.badRequest().body("Query can't be empty.");
            }
            if(!dto.getOperator().equals("AND") && !dto.getOperator().equals("OR") && !dto.getOperator().equals("AND NOT")){
                return ResponseEntity.badRequest().body("Operator is not valid.");
            }
        }

        List<SearchResultDTO> searchResultDTOS = this.searchService.advancedSearch(searchAdvancedDTOS);
        return new ResponseEntity(searchResultDTOS, HttpStatus.OK);
    }



    @GetMapping(value = "/betaReaders/{genre}")
    public ResponseEntity findBetaReaders(@PathVariable("genre") String genre) throws IOException {

        if(genre.trim().equals("")){
            return ResponseEntity.badRequest().body("Genre cannot be empty.");
        }

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        List<BRSearchResultDTO> result = this.searchService.findBetaReadersForGenre(genre, currentUser.getName());
        return new ResponseEntity(result, HttpStatus.OK);

    }





}
