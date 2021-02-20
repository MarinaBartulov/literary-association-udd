package team16.literaryassociation.elasticsearch.controller;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;

import java.util.List;

@RestController
@RequestMapping(value = "/api/search")
public class SearchController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;



    @PostMapping(value = "/basic")
    public ResponseEntity basicSearch(){

        return ResponseEntity.ok().build();
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
