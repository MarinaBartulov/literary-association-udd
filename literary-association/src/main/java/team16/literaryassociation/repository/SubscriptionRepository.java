package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team16.literaryassociation.model.Subscription;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    @Query(value = "select s from Subscription s where s.reader.id = ?1 and s.merchant.id = ?2 and s.expirationDate > ?3 ")
    List<Subscription> readerHasSubscription(Long readerId, Long merchantId, LocalDate now);

    @Query(value = "from Subscription s where s.status = 'INITIATED' or s.status = 'CREATED'")
    List<Subscription> findAllUnfinishedSubscriptions();

    @Query(value = "from Subscription s join s.reader r where r.username = ?1")
    List<Subscription> getSubscriptions(String username);
}
