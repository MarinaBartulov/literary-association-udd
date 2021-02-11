package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.literaryassociation.model.Book;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailsDTO {

    private Long id;
    private String title;
    private String genre;
    private String writer;
    private String publisher;
    private Long publisherId;
    private double price;
    private String ISBN;
    private String synopsis;
    private boolean openAccess;
    private String pdf;
    private String publishersAddress;
    private String year;
    private int numOfPages;

    public BookDetailsDTO(Book book){
        this.id = book.getId();
        this.title = book.getTitle();
        this.genre = book.getGenre().getName();
        this.writer = book.getWriter().getFirstName() + " " + book.getWriter().getLastName();
        this.publisher = book.getPublisher().getMerchantName();
        this.publisherId = book.getPublisher().getId();
        this.price = book.getPrice();
        this.ISBN = book.getISBN();
        this.synopsis = book.getSynopsis();
        this.openAccess = book.isOpenAccess();
        this.pdf = book.getPdf();
        this.publishersAddress = book.getPublishersAddress();
        this.year = book.getYear();
        this.numOfPages = book.getNumOfPages();
    }
}
