package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.MembershipApplication;

import java.util.List;

@Repository
public interface MembershipApplicationRepository extends JpaRepository<MembershipApplication, Long> {

    //@Query(value = "select ma from MembershipApplication ma where ma.active = true")
    //List<MembershipApplication> findAllActive();

    @Query(value = "select ma from MembershipApplication ma where ma.processId = ?1")
    List<MembershipApplication> getOneByProcessId(String processId);
}
