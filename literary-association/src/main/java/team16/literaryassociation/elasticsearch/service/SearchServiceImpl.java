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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import team16.literaryassociation.elasticsearch.dto.BRSearchResultDTO;
import team16.literaryassociation.elasticsearch.dto.SearchAdvancedDTO;
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
import java.util.stream.Collectors;

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
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(searchBasicDTO.getQuery()).fields(map));
            }else{
                boolQueryBuilder.must(QueryBuilders.matchQuery(searchBasicDTO.getField(),searchBasicDTO.getQuery()));
            }
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder();

        if(searchBasicDTO.getField().equals("all")){
            highlightBuilder.field("title").field("content").field("writer").field("genre");
        }else {
            highlightBuilder.field(searchBasicDTO.getField());
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .build();
        SearchHits<BookIndexUnit> searchHits =  this.elasticsearchTemplate.search(searchQuery, BookIndexUnit.class, IndexCoordinates.of("books_index"));
        List<SearchResultDTO> results = this.getResult(searchHits);
        System.out.println("Pronadjeno knjiga: " + results.size());
        return results;
    }

    @Override
    public List<SearchResultDTO> advancedSearch(List<SearchAdvancedDTO> searchAdvancedDTOS) throws IOException {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        for(SearchAdvancedDTO dto : searchAdvancedDTOS){

            if(dto.getOperator().equals("AND")){
                if(dto.isPhrase()){
                    if(dto.getField().equals("all")){
                        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(dto.getQuery(), "title", "writer", "content", "genre").type("phrase"));
                    }else {
                        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(dto.getField(), dto.getQuery()));
                    }
                }else{
                    if(dto.getField().equals("all")) {
                        Map<String, Float> map = new HashMap<>();
                        map.put("title", 1.0f);
                        map.put("writer", 1.0f);
                        map.put("content",1.0f);
                        map.put("genre",1.0f);
                        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(dto.getQuery()).fields(map));
                    }else{
                        boolQueryBuilder.must(QueryBuilders.matchQuery(dto.getField(),dto.getQuery()));

                    }
                }
            }else if(dto.getOperator().equals("OR")){
                if(dto.isPhrase()){
                    if(dto.getField().equals("all")){
                        boolQueryBuilder.should(QueryBuilders.multiMatchQuery(dto.getQuery(), "title", "writer", "content", "genre").type("phrase"));
                    }else {
                        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(dto.getField(), dto.getQuery()));
                    }
                }else{
                    if(dto.getField().equals("all")) {
                        Map<String, Float> map = new HashMap<>();
                        map.put("title", 1.0f);
                        map.put("writer", 1.0f);
                        map.put("content",1.0f);
                        map.put("genre",1.0f);
                        boolQueryBuilder.should(QueryBuilders.multiMatchQuery(dto.getQuery()).fields(map));
                    }else{
                        boolQueryBuilder.should(QueryBuilders.matchQuery(dto.getField(),dto.getQuery()));
                    }
                }
            }else{
                if(dto.isPhrase()){
                    if(dto.getField().equals("all")){
                        boolQueryBuilder.mustNot(QueryBuilders.multiMatchQuery(dto.getQuery(), "title", "writer", "content", "genre").type("phrase"));
                    }else {
                        boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(dto.getField(), dto.getQuery()));
                    }
                }else{
                    if(dto.getField().equals("all")) {
                        Map<String, Float> map = new HashMap<>();
                        map.put("title", 1.0f);
                        map.put("writer", 1.0f);
                        map.put("content",1.0f);
                        map.put("genre",1.0f);
                        boolQueryBuilder.mustNot(QueryBuilders.multiMatchQuery(dto.getQuery()).fields(map));
                    }else{
                        boolQueryBuilder.mustNot(QueryBuilders.matchQuery(dto.getField(),dto.getQuery()));
                    }
                }
            }
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder();

        List<String> highlightedFields = new ArrayList<>();
        for(SearchAdvancedDTO dto : searchAdvancedDTOS){
            if(dto.getOperator().equals("AND") || dto.getOperator().equals("OR")) {
                if (dto.getField().equals("all")) {
                    highlightedFields.add("title");
                    highlightedFields.add("content");
                    highlightedFields.add("genre");
                    highlightedFields.add("writer");
                } else {
                    highlightedFields.add(dto.getField());
                }
            }

        }
        highlightedFields = highlightedFields.stream().distinct().collect(Collectors.toList());

        for(String field : highlightedFields){
            highlightBuilder.field(field);
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .build();
        SearchHits<BookIndexUnit> searchHits =  this.elasticsearchTemplate.search(searchQuery, BookIndexUnit.class, IndexCoordinates.of("books_index"));
        List<SearchResultDTO> results = this.getResult(searchHits);
        System.out.println("Pronadjeno knjiga: " + results.size());
        return results;
    }


    @Override
    public List<BRSearchResultDTO> findBetaReadersForGenre(String genre, String writerUsername) throws IOException {

        Writer writer = this.writerService.findByUsername(writerUsername);
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("d3ab24f7cbe84dc7b61a1be57b553ae6");
        String query = writer.getCity() + ", " + writer.getCountry();
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(query);
        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        JOpenCageLatLng locationLatLng = response.getFirstPosition();

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.geoDistanceQuery("location").point(locationLatLng.getLat(),locationLatLng.getLng()).distance("100km"));
        queryBuilder.must(QueryBuilders.matchQuery("genres", genre));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        SearchHits<BetaReaderIndexUnit> searchHits =  this.elasticsearchTemplate.search(searchQuery, BetaReaderIndexUnit.class, IndexCoordinates.of("beta_readers_index"));
        List<BRSearchResultDTO> results = new ArrayList<>();

        for(SearchHit<BetaReaderIndexUnit> hit : searchHits) {
            BetaReaderIndexUnit br = hit.getContent();
            BRSearchResultDTO dto = new BRSearchResultDTO(br);
            results.add(dto);

        }
        for(BRSearchResultDTO bt : results) {
            System.out.println("Found " + bt.getFullName());
        }
        return results;
    }


    private List<SearchResultDTO> getResult(SearchHits<BookIndexUnit> searchHits){

        List<SearchResultDTO> results = new ArrayList<>();

        for(org.springframework.data.elasticsearch.core.SearchHit<BookIndexUnit> hit : searchHits){

            SearchResultDTO dto = new SearchResultDTO();
            BookIndexUnit bi = hit.getContent();
            dto.setTitle(bi.getTitle());
            dto.setId(bi.getId());
            dto.setGenre(bi.getGenre());
            dto.setWriter(bi.getWriter());
            dto.setOpenAccess(bi.isOpenAccess());
            dto.setPdf(bi.getPdf());

            String highlights = "... ";

            Map<String, List<String>> highlightFieldMap = hit.getHighlightFields();
            if(highlightFieldMap.isEmpty()){
                highlights += hit.getContent().getContent().substring(0,350) + "... ";
            }

            for(Map.Entry<String, List<String>> h : highlightFieldMap.entrySet()){
                for(String str : h.getValue()){
                    highlights += str + "... ";
                }
            }

            highlights = highlights.replace("<em>","<em><b>");
            highlights = highlights.replace("</em>", "</b></em>");
            System.out.println("Highlight: " + highlights);
            dto.setHighlights(highlights);
            results.add(dto);

        }
        return results;
    }
}
