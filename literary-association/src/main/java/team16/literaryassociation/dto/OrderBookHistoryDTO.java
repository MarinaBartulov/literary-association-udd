package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.literaryassociation.model.OrderBook;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderBookHistoryDTO {

    private Long bookId;
    private String bookTitle;
    private String genre;
    private String writer;
    private double price;
    private int amount;

    public OrderBookHistoryDTO(OrderBook orderBook){
        this.bookId = orderBook.getBook().getId();
        this.bookTitle = orderBook.getBook().getTitle();
        this.genre = orderBook.getBook().getGenre().getName();
        this.writer = orderBook.getBook().getWriter().getFirstName() + " " + orderBook.getBook().getWriter().getLastName();
        this.price = orderBook.getBook().getPrice();
        this.amount = orderBook.getAmount();
    }

}
