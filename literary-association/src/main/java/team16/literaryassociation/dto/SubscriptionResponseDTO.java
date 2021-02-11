package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.enums.SubscriptionFrequency;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {

    private Long subscriptionId; // id na PSP

    private LocalDateTime createdAd;

    private Double discount;

    private LocalDate expirationDate;

    private Double price;

    private Integer cycles;

    private SubscriptionFrequency frequency;

    private String currency;

    private String merchantEmail;

    private String redirectURL;

}
