package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.dto.SubscriptionResponseDTO;
import team16.literaryassociation.enums.SubscriptionFrequency;
import team16.literaryassociation.enums.SubscriptionStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long subscriptionId; // id na PSP

    @Column
    private LocalDateTime createdAt; // kada je kreirana na PSP

    @Column
    private LocalDate expirationDate;

    @Column
    private Double price;

    @Column
    private Double discount;

    @Column
    private Integer cycles;

    @Column
    private String currency;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionFrequency frequency;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @ManyToOne
    private Merchant merchant;

    @ManyToOne
    private Reader reader;

    public Subscription(SubscriptionResponseDTO dto, Merchant merchant, Reader reader)
    {
        this.subscriptionId = dto.getSubscriptionId();
        this.price = dto.getPrice();
        this.currency = dto.getCurrency();
        this.cycles = dto.getCycles();
        this.frequency = dto.getFrequency();
        this.merchant = merchant;
        this.reader = reader;
        this.expirationDate = dto.getExpirationDate();
        this.status = SubscriptionStatus.INITIATED;
        this.createdAt = dto.getCreatedAd();
        this.discount = dto.getDiscount();
    }
}
