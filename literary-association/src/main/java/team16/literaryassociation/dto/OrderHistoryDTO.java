package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDTO {

    private Long id;
    private double total;
    private LocalDateTime dateCreated;
    private String orderStatus;
    private String merchant;
    private List<OrderBookHistoryDTO> books = new ArrayList<>();
}
