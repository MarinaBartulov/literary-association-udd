package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MerchantActivationDTO {

    @NotNull
    @Email
    private String merchantEmail;
    //private boolean bankPaymentMethod;
    //private String merchantId;
    //private String merchantPassword;
}
