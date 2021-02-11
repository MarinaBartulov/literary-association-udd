package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.EditorPlagiarismNote;

@Repository
public interface EditorPlagiarismNoteRepository extends JpaRepository<EditorPlagiarismNote, Long> {
}
