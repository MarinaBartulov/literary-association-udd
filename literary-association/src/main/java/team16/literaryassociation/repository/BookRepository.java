package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select * from book b where b.writer_id = ?1", nativeQuery = true)
    List<Book> findAllWritersBooks(Long writerId);

    @Query(value = "select * from book b where b.writer_id = ?1 and b.title = ?2", nativeQuery = true)
    Book findBookByWriterAndTitle(Long writerId, String title);

    @Query(value = "select * from book b, user u where b.writer_id = u.id and lower(b.title) like lower(concat('%', ?1, '%')) and lower(u.first_name) like lower(concat('%', ?2, '%')) and lower(u.last_name) like lower(concat('%', ?3, '%'))", nativeQuery = true)
    Book findBookByTitleAndWritersNameIgnoreCase(String title, String writersFirstName, String writersLastName);

    @Query(value = "from Book b join b.publisher p where p.activated = true")
    List<Book> findAllBook();
}
