package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.LiteraryWork;

import java.util.List;

@Repository
public interface LiteraryWorkRepository extends JpaRepository<LiteraryWork, Long> {

    @Query(value = "select lw from LiteraryWork lw where lw.membershipApplication.id = ?1")
    List<LiteraryWork> findAllWithMembershipApplicationId(Long id);
}
