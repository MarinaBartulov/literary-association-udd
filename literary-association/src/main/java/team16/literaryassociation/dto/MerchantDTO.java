package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.literaryassociation.model.Merchant;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDTO {

    private Long id;
    @NotNull(message = "Name is required.")
    private String name;
    @NotNull(message = "Email is required.")
    @Email(message = "Email is not valid.")
    private String email;

    public MerchantDTO(Merchant merchant){
        this.id = merchant.getId();
        this.name = merchant.getMerchantName();
        this.email = merchant.getMerchantEmail();
    }
}
