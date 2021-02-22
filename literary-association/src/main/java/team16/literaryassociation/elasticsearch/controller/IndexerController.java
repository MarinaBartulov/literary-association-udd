package team16.literaryassociation.elasticsearch.controller;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import org.apache.commons.compress.utils.Lists;
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
import java.util.Collection;
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

        System.out.println("Uslo u indeksiranje knjiga");
        this.bookIndexRepository.deleteAll();
        List<Book> books = this.bookService.getBooks();
        System.out.println("Books indexing started...");
        for(Book book : books){
            System.out.println("Book: " + book.getTitle());
            BookIndexUnit biu = new BookIndexUnit();
            biu.setId(book.getId());
            biu.setTitle(book.getTitle());
            biu.setGenre(book.getGenre().getName());
            biu.setOpenAccess(book.isOpenAccess());
            biu.setPdf(book.getPdf());
            biu.setWriter(book.getWriter().getFirstName() + " " + book.getWriter().getLastName());
            String content = this.contentService.getContent(book.getPdf());
            if(content == null) {
                System.out.println("Content not extracted.");
                return ResponseEntity.badRequest().body("Error occurred while extracting content.");
            }
            biu.setContent(content);
            this.bookIndexRepository.save(biu);
            System.out.println("Book is indexed");
        }

        System.out.println("Books indexing finished.");

        return ResponseEntity.ok().build();

    }

    @GetMapping(value = "/betaReaders")
    public ResponseEntity indexBetaReaders() {

        System.out.println("Uslo u indeksiranje beta-readera");
        this.betaReaderIndexRepository.deleteAll();
        List<Reader> betaReaders = this.readerService.getAllBetaReaders();
        System.out.println("Beta-readers indexing started...");

        for(Reader r : betaReaders){
            BetaReaderIndexUnit br = new BetaReaderIndexUnit();
            br.setId(r.getId());
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

        System.out.println("Beta-readers indexing finished.");
        List<BetaReaderIndexUnit> betaReadersFound = this.betaReaderIndexRepository.findAll().getContent();
        System.out.println("Ukupno beta readera: " + betaReadersFound.size());
        return ResponseEntity.ok().build();

    }


}
