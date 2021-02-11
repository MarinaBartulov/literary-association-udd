package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.dto.BookDTO;
import team16.literaryassociation.dto.BookDetailsDTO;
import team16.literaryassociation.model.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);
    Book findOne(Long id);
    List<Book> findAllWritersBooks(Long writerId);
    Book findBookByWriterAndTitle(Long writerId, String title);
    Book findBookByTitleAndWritersName(String title, String writerFirstName, String writerLastName);
    List<BookDTO> getAllBooks();
    BookDetailsDTO getBookDetails(Long id);
    Book findById(Long id);
}
