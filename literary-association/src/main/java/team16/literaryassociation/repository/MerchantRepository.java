package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.Merchant;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Merchant findByMerchantEmail(String email);
    @Query(value = "select m from Merchant m where m.activated = true")
    List<Merchant> findAllActiveMerchants();
}
