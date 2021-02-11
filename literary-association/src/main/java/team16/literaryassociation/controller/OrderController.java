package team16.literaryassociation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import team16.literaryassociation.dto.*;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.services.interfaces.MerchantService;
import team16.literaryassociation.services.interfaces.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderRequestDTO dto){

        Merchant merchant = this.merchantService.findOne(dto.getMerchantId());
        if(merchant == null){
            return ResponseEntity.badRequest().body("Merchant with that id doesn't exist.");
        }
        OrderResponseDTO orderResponseDTO = this.orderService.createOrder(dto, merchant);
        return ResponseEntity.ok(orderResponseDTO);
    }

    @GetMapping
    public ResponseEntity getOrders(){

        List<OrderHistoryDTO> ordersDTO = this.orderService.getOrders();
        return new ResponseEntity(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/purchasedBooks")
    public ResponseEntity getPurchasedBooks(){

        List<BookDTO> purchasedBooksDTO = this.orderService.getPurchasedBooks();
        return new ResponseEntity(purchasedBooksDTO, HttpStatus.OK);
    }
}
