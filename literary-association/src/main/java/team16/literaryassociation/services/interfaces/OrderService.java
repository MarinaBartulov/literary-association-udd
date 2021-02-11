package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.dto.BookDTO;
import team16.literaryassociation.dto.OrderHistoryDTO;
import team16.literaryassociation.dto.OrderRequestDTO;
import team16.literaryassociation.dto.OrderResponseDTO;
import team16.literaryassociation.model.Merchant;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, Merchant merchant);
    List<OrderHistoryDTO> getOrders();
    List<BookDTO> getPurchasedBooks();
}
