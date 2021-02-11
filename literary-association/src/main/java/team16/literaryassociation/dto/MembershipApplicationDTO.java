package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.model.MembershipApplication;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MembershipApplicationDTO {

    protected Long id;

    private String processId;

    private Double price;

    //zbog fronta

    private String writerFirstName;

    private String writerLastName;

    public MembershipApplicationDTO(MembershipApplication membershipApplication)
    {
        this.id = membershipApplication.getId();
        this.processId = membershipApplication.getProcessId();
        this.price = membershipApplication.getPrice();
        this.writerFirstName = membershipApplication.getWriter().getFirstName();
        this.writerLastName = membershipApplication.getWriter().getLastName();
    }
}
