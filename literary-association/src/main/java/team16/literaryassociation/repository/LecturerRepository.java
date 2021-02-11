package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.Lecturer;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
}
