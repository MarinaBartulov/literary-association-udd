package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.BookRequest;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
}
