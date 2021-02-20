package team16.literaryassociation.elasticsearch.controller;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;
import team16.literaryassociation.elasticsearch.model.BookIndexUnit;
import team16.literaryassociation.elasticsearch.repository.BetaReaderIndexRepository;
import team16.literaryassociation.elasticsearch.repository.BookIndexRepository;
import team16.literaryassociation.elasticsearch.service.ContentService;
import team16.literaryassociation.model.Book;
import team16.literaryassociation.model.Genre;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.ReaderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/index")
public class IndexerController {

    @Autowired
    private BookService bookService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private BookIndexRepository bookIndexRepository;
    @Autowired
    private BetaReaderIndexRepository betaReaderIndexRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Autowired
    private ContentService contentService;

    @GetMapping(value = "/books")
    public ResponseEntity indexBooks() throws IOException {

        List<Book> books = this.bookService.getBooks();
        System.out.println("Broj pronadjenih knjiga: " + books.size());
        for(Book book : books){
            System.out.println("Indeksiranje knjige " + book.getTitle());
            BookIndexUnit biu = new BookIndexUnit();
            biu.setId(book.getId());
            biu.setTitle(book.getTitle());
            biu.setGenre(book.getGenre().getName());
            biu.setOpenAccess(book.isOpenAccess());
            biu.setWriter(book.getWriter().getFirstName() + " " + book.getWriter().getLastName());
            String content = this.contentService.getContent(book.getPdf());
            System.out.println("Content uspesno ekstrahovan");
            biu.setContent(content);
            this.bookIndexRepository.save(biu);
            System.out.println("Knjiga indeksirana");
        }

        return ResponseEntity.ok().build();

    }

    @GetMapping(value = "/betaReaders")
    public ResponseEntity indexBetaReaders() {

        List<Reader> betaReaders = this.readerService.getAllBetaReaders();

        for(Reader r : betaReaders){
            BetaReaderIndexUnit br = new BetaReaderIndexUnit();
            br.setBetaReaderId(r.getId());
            br.setFullName(r.getFirstName() + " " + r.getLastName());

            String genres = "";
            for(Genre g : r.getBetaGenres()){
                System.out.println(g.getName());
                genres += g.getName() + " ";
            }
            br.setGenres(genres);

            JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("d3ab24f7cbe84dc7b61a1be57b553ae6");
            String query = r.getCity() + ", " + r.getCountry();
            JOpenCageForwardRequest request = new JOpenCageForwardRequest(query);
            JOpenCageResponse response = jOpenCageGeocoder.forward(request);
            JOpenCageLatLng locationLatLng = response.getFirstPosition();
            System.out.println(locationLatLng.getLat());
            System.out.println(locationLatLng.getLng());
            br.setLocation(new GeoPoint(locationLatLng.getLat(),locationLatLng.getLng()));
            this.betaReaderIndexRepository.save(br);
            System.out.println("Indeksiran");
        }

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
