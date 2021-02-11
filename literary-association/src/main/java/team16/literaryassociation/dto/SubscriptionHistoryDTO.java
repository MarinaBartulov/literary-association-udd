package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.enums.SubscriptionFrequency;
import team16.literaryassociation.enums.SubscriptionStatus;
import team16.literaryassociation.model.Merchant;
import team16.literaryassociation.model.Reader;
import team16.literaryassociation.model.Subscription;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriptionHistoryDTO {

    private Long id;

    private LocalDateTime createdAt; // kada je kreirana na PSP

    private LocalDate expirationDate;

    private Double price;

    private Integer cycles;

    private String frequency;

    private String status;

    private String merchantName;

    private Double discount;

    public SubscriptionHistoryDTO(Subscription s){
        this.id = s.getId();
        this.createdAt = s.getCreatedAt();
        this.expirationDate = s.getExpirationDate();
        this.price = s.getPrice();
        this.cycles = s.getCycles();
        this.frequency = s.getFrequency().toString();
        this.status = s.getStatus().toString();
        this.merchantName = s.getMerchant().getMerchantName();
        this.discount = s.getDiscount();
    }
}
