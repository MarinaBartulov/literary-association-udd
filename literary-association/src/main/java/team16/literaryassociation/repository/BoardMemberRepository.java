package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.BoardMember;

import java.util.List;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

    @Query(value = "SELECT * FROM User bm WHERE bm.type = 'BoardMember'", nativeQuery = true)
    List<BoardMember> findAll();

}
