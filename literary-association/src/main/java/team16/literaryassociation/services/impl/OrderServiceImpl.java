package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.enums.OrderStatus;
import team16.literaryassociation.exception.BadRequestException;
import team16.literaryassociation.exception.NotFoundException;
import team16.literaryassociation.model.*;
import team16.literaryassociation.repository.OrderRepository;
import team16.literaryassociation.services.interfaces.BookService;
import team16.literaryassociation.services.interfaces.OrderService;
import team16.literaryassociation.services.interfaces.ReaderService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BookService bookService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO dto, Merchant merchant) {

        Authentication currentReader = SecurityContextHolder.getContext().getAuthentication();
        String username = currentReader.getName();
        Reader reader = this.readerService.findByUsername(username);

        for(OrderBookDTO ob: dto.getBooks()){
            Book book = this.bookService.findById(ob.getBookId());
            if(book == null){
                throw new NotFoundException("Book with id " + ob.getBookId() + " doesn't exits.");
            }
        }

        Order order = new Order();
        order.setTotal(dto.getTotal());
        order.setDateCreated(LocalDateTime.now());
        order.setReader(reader);
        order.setMerchant(merchant);
        order.setOrderStatus(OrderStatus.INITIATED);

        try {
            this.orderRepository.save(order);
        }catch(Exception e){
            throw new BadRequestException("Error occurred while saving a new order.");
        }

        Set<OrderBook> books = new HashSet<>();
        for(OrderBookDTO ob: dto.getBooks()){
            Book book = this.bookService.findById(ob.getBookId());
            OrderBook orderBook = new OrderBook();
            orderBook.setBook(book);
            orderBook.setAmount(ob.getAmount());
            orderBook.setOrder(order);
            books.add(orderBook);
        }

        order.setBooks(books);

        try {
            this.orderRepository.save(order);
        }catch(Exception e){
            throw new BadRequestException("Error occurred while saving a new order.");
        }

        ResponseEntity<OrderResponseDTO> response;
        try {
            response = restTemplate.postForEntity("https://localhost:8083/psp-service/api/order",
                    new OrderDTO(order.getId(), merchant.getMerchantEmail(),
                            "USD", dto.getTotal(), merchant.getMerchantSuccessUrl(), merchant.getMerchantFailedUrl(),
                            merchant.getMerchantErrorUrl()), OrderResponseDTO.class);
            System.out.println(response.getBody().getOrderId());
        }catch(Exception e){
            e.printStackTrace();
            throw new BadRequestException("Error occurred while sending order on payment concentrator.");
        }
        return response.getBody();
    }

    @Override
    public List<OrderHistoryDTO> getOrders() {

        Authentication currentReader = SecurityContextHolder.getContext().getAuthentication();
        String username = currentReader.getName();
        List<Order> orders = this.orderRepository.getOrders(username);
        List<OrderHistoryDTO> ordersDTO = new ArrayList<>();
        for(Order o : orders){
           OrderHistoryDTO orderDTO = new OrderHistoryDTO();
           orderDTO.setId(o.getId());
           orderDTO.setTotal(o.getTotal());
           orderDTO.setOrderStatus(o.getOrderStatus().toString());
           orderDTO.setDateCreated(o.getDateCreated());
           orderDTO.setBooks(o.getBooks().stream().map(or -> new OrderBookHistoryDTO(or)).collect(Collectors.toList()));
           orderDTO.setMerchant(o.getMerchant().getMerchantName());
           ordersDTO.add(orderDTO);
        }
        return ordersDTO;
    }

    @Override
    public List<BookDTO> getPurchasedBooks() {

        Authentication currentReader = SecurityContextHolder.getContext().getAuthentication();
        String username = currentReader.getName();
        List<Order> completedOrders = this.orderRepository.findCompletedOrdersForReader(username);
        List<BookDTO> purchasedBooks = new ArrayList<>();
        for(Order o : completedOrders){
            for(OrderBook ob: o.getBooks()){
                BookDTO b = new BookDTO(ob.getBook());
                purchasedBooks.add(b);
            }
        }
        return purchasedBooks;
    }

    @Scheduled(initialDelay = 45000, fixedRate = 300000) //na svakih 5 minuta
    public void updateOrdersStatus(){

        System.out.println("Updating orders status started...");
        //pronadju se INITIATED i CREATED
       List<Order> unfinishedOrders = this.orderRepository.findAllUnfinishedOrders();

       for(Order o : unfinishedOrders){

             ResponseEntity<OrderStatusDTO> response = null;
             try{
                 //mora i email jer pc mogu koristiti razlicite aplikacije a one mogu imati ordere sa istim id
                 String merchantEmail = o.getMerchant().getMerchantEmail();
                 response = restTemplate.getForEntity("https://localhost:8083/psp-service/api/order/status?orderId=" + o.getId() + "&merchantEmail=" + merchantEmail, OrderStatusDTO.class);

             }catch(Exception e){
                 e.printStackTrace();
                 return;
             }

             if(response.getBody().getStatus() != null) {
                 OrderStatus status = OrderStatus.valueOf(response.getBody().getStatus());
                 if (!status.equals(o.getOrderStatus())) {
                     System.out.println("Promenjen status sa " + o.getOrderStatus().toString() + " na " + status.toString());
                     o.setOrderStatus(status);
                     this.orderRepository.save(o);
                 }
             }
       }
        System.out.println("Updating orders status finished...");

    }
}
