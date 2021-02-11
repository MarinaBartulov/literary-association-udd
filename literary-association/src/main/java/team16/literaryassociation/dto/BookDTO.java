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
public class BookDTO {

    private Long id;
    private String title;
    private String genre;
    private String writer;
    private String publisher;
    private Long publisherId;
    private double price;

    public BookDTO(Book book){
        this.id = book.getId();
        this.title = book.getTitle();
        this.genre = book.getGenre().getName();
        this.writer = book.getWriter().getFirstName() + " " + book.getWriter().getLastName();
        this.publisher = book.getPublisher().getMerchantName();
        this.publisherId = book.getPublisher().getId();
        this.price = book.getPrice();
    }
}
