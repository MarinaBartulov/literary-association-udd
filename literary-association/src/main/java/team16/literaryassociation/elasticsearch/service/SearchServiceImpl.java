package team16.literaryassociation.elasticsearch.service;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.google.gson.JsonObject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import team16.literaryassociation.elasticsearch.dto.BRSearchResultDTO;
import team16.literaryassociation.elasticsearch.dto.SearchAdvancedDTO;
import team16.literaryassociation.elasticsearch.dto.SearchBasicDTO;
import team16.literaryassociation.elasticsearch.dto.SearchResultDTO;
import team16.literaryassociation.elasticsearch.dto.plagiator.PaperDTO;
import team16.literaryassociation.elasticsearch.dto.plagiator.PaperResultPlagiatorDTO;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;
import team16.literaryassociation.elasticsearch.model.BookIndexUnit;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Writer;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.WriterService;

import java.io.*;
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
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BookService bookService;

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

    @Override
    public String checkPlagiarism(MultipartFile file) throws IOException {

        //prvo moram da posaljem zahtev za logovanje
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonObject loginJsonObject = new JsonObject();
        loginJsonObject.addProperty("email", "literaryassociation7@gmail.com");
        loginJsonObject.addProperty("password", "password123");

        HttpEntity<String> request =
                new HttpEntity<String>(loginJsonObject.toString(), headers);
        String jwt = "";
        try {
            jwt = restTemplate.postForObject("http://localhost:8081/api/login", request, String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        jwt = jwt.substring(1,jwt.length()-1);
        System.out.println("Jwt je " + jwt);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + jwt);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        File targetFile = new File("D:\\tempFolder\\" + file.getOriginalFilename());
        try {
            targetFile.createNewFile();
            file.transferTo(targetFile);
        }catch(Exception e){
            e.printStackTrace();
        }

        body.add("file", new FileSystemResource(targetFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        ResponseEntity<PaperResultPlagiatorDTO> response = null;
        try {
            response = restTemplate
                    .postForEntity("http://localhost:8081/api/file/upload/new", requestEntity, PaperResultPlagiatorDTO.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        PaperResultPlagiatorDTO resultResponse = response.getBody();
        targetFile.delete();

        String baseUrl = "https://localhost:8080/api/task/downloadFile?filePath=";
        String result = "";

        System.out.println("Plagijati");
        for(PaperDTO similarPaper: resultResponse.getSimilarPapers()){
            String title = similarPaper.getTitle();
            System.out.println(title + " : " + similarPaper.getSimilarProcent());
            List<Book> books = this.bookService.findByPdfEndsWith(title);
            if(books.size() > 0) {
                Book book = books.get(0);
                result += baseUrl + book.getPdf() + "|";

            }
        }
        result = result.substring(0, result.length()-1);

        //String result = "https://localhost:8080/api/task/downloadFile?filePath=files-uploaded/LiterarnoUdruzenje.pdf";
        return result;
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
