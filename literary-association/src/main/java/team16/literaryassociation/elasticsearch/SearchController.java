package team16.literaryassociation.elasticsearch;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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





}
