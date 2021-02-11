package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team16.literaryassociation.model.Reader;

import java.util.List;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

    @Query(value = "select * from user u where u.beta_reader = true", nativeQuery = true)
    List<Reader> findAllBetReaders();

    @Query(value = "select * from user u, beta_reader_genre brg, genre g where u.id = brg.beta_reader_id and brg.genre_id = g.id and g.name = ?1 and u.beta_reader = true", nativeQuery = true)
    List<Reader> findAllBetReadersForGenre(String name);

    Reader findByUsername(String username);
}
