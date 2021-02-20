package team16.literaryassociation.elasticsearch.service;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import team16.literaryassociation.elasticsearch.dto.SearchBasicDTO;
import team16.literaryassociation.elasticsearch.dto.SearchResultDTO;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;
import team16.literaryassociation.elasticsearch.model.BookIndexUnit;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.WriterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private WriterService writerService;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public List<SearchResultDTO> basicSearch(SearchBasicDTO searchBasicDTO) throws IOException {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if(searchBasicDTO.isPhrase()){
            if(searchBasicDTO.getField().equals("all")){
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(searchBasicDTO.getQuery(), "title", "writer", "content", "genre").type("phrase"));
            }else {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(searchBasicDTO.getField(), searchBasicDTO.getQuery()));
            }
        }else{
            if(searchBasicDTO.getField().equals("all")) {
                Map<String, Float> map = new HashMap<>();
                map.put("title", 1.0f);
                map.put("writer", 1.0f);
                map.put("content",1.0f);
                map.put("genre",1.0f);
                boolQueryBuilder.must(QueryBuilders.simpleQueryStringQuery(searchBasicDTO.getQuery()).fields(map));
            }else{
                boolQueryBuilder.must(QueryBuilders.simpleQueryStringQuery(searchBasicDTO.getQuery()).field(searchBasicDTO.getField()));
            }
        }

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        if(searchBasicDTO.getField().equals("all")){
            highlightBuilder.field("title").field("content").field("writer").field("genre");
        }else {
            highlightBuilder.field(searchBasicDTO.getField());
        }

        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse =  this.elasticsearchTemplate.getClient().search(searchRequest, RequestOptions.DEFAULT);
        List<SearchResultDTO> results = this.getResult(searchResponse);
        System.out.println("Pronadjeno knjiga: " + results.size());
        return results;
    }

    @Override
    public List<BetaReaderIndexUnit> findBetaReadersForGenre(String genre, String writerUsername) {

        Writer writer = this.writerService.findByUsername(writerUsername);
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("d3ab24f7cbe84dc7b61a1be57b553ae6");
        String query = writer.getCity() + ", " + writer.getCountry();
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(query);
        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        JOpenCageLatLng locationLatLng = response.getFirstPosition();

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.geoDistanceQuery("location").geoDistance(GeoDistance.ARC).point(locationLatLng.getLat(),locationLatLng.getLng()).distance("100km"));
        queryBuilder.must(QueryBuilders.queryStringQuery(genre).field("genres"));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        List<BetaReaderIndexUnit> betaReadersFound = this.elasticsearchTemplate.queryForList(searchQuery, BetaReaderIndexUnit.class);
        System.out.println(betaReadersFound.size());
        for(BetaReaderIndexUnit bt : betaReadersFound){
            System.out.println("Found " + bt.getFullName());
        }
        return betaReadersFound;
    }



    private List<SearchResultDTO> getResult(SearchResponse response){

        List<SearchResultDTO> results = new ArrayList<>();
        Gson gson = new Gson();

        for(SearchHit hit : response.getHits()){

            SearchResultDTO dto = gson.fromJson(hit.getSourceAsString(), SearchResultDTO.class);
            String highlights = "... ";

            Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();

            for(Map.Entry<String, HighlightField> h : highlightFieldMap.entrySet()){

                Text[] fragments = h.getValue().fragments();
                for(Text text : fragments){
                    highlights += text.string() + "... ";
                }
            }

            System.out.println("Highlight: " + highlights);
            dto.setHighlights(highlights);
            results.add(dto);

        }
        return results;
    }
}
