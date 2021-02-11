package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team16.literaryassociation.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "from Order o join o.reader r where r.username = ?1")
    List<Order> getOrders(String username);

    @Query(value = "from Order o join o.reader r where r.username = ?1 and o.orderStatus = 'COMPLETED'")
    List<Order> findCompletedOrdersForReader(String username);

    @Query(value = "from Order o where o.orderStatus = 'INITIATED' or o.orderStatus = 'CREATED'")
    List<Order> findAllUnfinishedOrders();
}
