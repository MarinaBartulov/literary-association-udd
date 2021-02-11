package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MerchantPCDTO {

    private String merchantName;
    private String merchantEmail;
    private String activationUrl;
    private String appId;
    private String successUrl;
    private String errorUrl;
    private String failedUrl;
}
