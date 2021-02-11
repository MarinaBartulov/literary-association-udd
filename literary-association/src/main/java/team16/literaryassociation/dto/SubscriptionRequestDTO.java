package team16.literaryassociation.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDTO {

    @NotNull
    @Positive
    private Long billingPlanId;

    @NotNull
    private String currency;

    @NotNull
    private Long merchantId; // id na LA

    private String merchantEmail; // da se moze pronaci na PSP

}
