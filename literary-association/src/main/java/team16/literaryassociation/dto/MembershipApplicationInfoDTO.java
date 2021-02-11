package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.model.LiteraryWork;
import team16.literaryassociation.model.MembershipApplication;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MembershipApplicationInfoDTO {

    protected Long id;

    private Integer moreMaterialRequested;

    private List<LiteraryWorkDTO> literaryWorks;

    private String processId;

    private Double price;

    //zbog fronta

    private String writerFirstName;

    private String writerLastName;

    public MembershipApplicationInfoDTO(MembershipApplication membershipApplication, List<LiteraryWork> literaryWorks)
    {
        this.id = membershipApplication.getId();
        this.price = membershipApplication.getPrice();
        this.processId = membershipApplication.getProcessId();
        this.writerFirstName = membershipApplication.getWriter().getFirstName();
        this.writerLastName = membershipApplication.getWriter().getLastName();
        this.literaryWorks = new ArrayList<>();

        for(LiteraryWork literaryWork : literaryWorks)
        {
            LiteraryWorkDTO literaryWorkDTO = new LiteraryWorkDTO(literaryWork);
            this.literaryWorks.add(literaryWorkDTO);
        }
    }
}
