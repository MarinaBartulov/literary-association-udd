package team16.literaryassociation.elasticsearch.controller;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team16.literaryassociation.elasticsearch.dto.SearchBasicDTO;
import team16.literaryassociation.elasticsearch.dto.SearchResultDTO;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;
import team16.literaryassociation.elasticsearch.service.SearchService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/search")
public class SearchController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private SearchService searchService;



    @PostMapping(value = "/basic")
    public ResponseEntity basicSearch(@RequestBody SearchBasicDTO searchBasicDTO) throws IOException {

        if(!searchBasicDTO.getField().equals("title") && !searchBasicDTO.getField().equals("writer")
        && !searchBasicDTO.getField().equals("content") && !searchBasicDTO.getField().equals("genre")
        && !searchBasicDTO.getField().equals("all")){
            return ResponseEntity.badRequest().body("You can't search by given field.");
        }
        if(searchBasicDTO.getQuery().trim().equals("")){
            return ResponseEntity.badRequest().body("Query can't be empty.");
        }

        List<SearchResultDTO> searchResultDTOS = this.searchService.basicSearch(searchBasicDTO);
        return new ResponseEntity(searchResultDTOS, HttpStatus.OK);
    }

    @PostMapping(value = "/advanced")
    public ResponseEntity advancedSearch(){

        return ResponseEntity.ok().build();
    }



    @GetMapping(value = "/findBetaReaders")
    public ResponseEntity findBetaReaders() {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.geoDistanceQuery("location").geoDistance(GeoDistance.ARC).point(45.26553,19.8294194).distance("100km"));
        queryBuilder.must(QueryBuilders.queryStringQuery("Crime").field("genres"));
        queryBuilder.must(QueryBuilders.queryStringQuery("Classic").field("genres"));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        List<BetaReaderIndexUnit> betaReadersFound = this.elasticsearchTemplate.queryForList(searchQuery, BetaReaderIndexUnit.class);
        System.out.println(betaReadersFound.size());
        for(BetaReaderIndexUnit bt : betaReadersFound){
            System.out.println("Found " + bt.getFullName());
        }

        return ResponseEntity.ok().build();

    }





}
