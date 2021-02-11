package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.BoardOpinion;

public interface BoardOpinionRepository extends JpaRepository<BoardOpinion, Long> {
}
