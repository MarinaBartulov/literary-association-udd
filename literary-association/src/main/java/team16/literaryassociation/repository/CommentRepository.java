package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
