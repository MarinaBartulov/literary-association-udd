package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.Writer;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {

    Writer findByUsername(String username);
}
