package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.Manuscript;

public interface ManuscriptRepository extends JpaRepository<Manuscript, Long> {
}
