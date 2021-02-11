package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingPlanDTO {

    private Long id;

    private Double price;

    private Double discount;

    private String type;

    private String frequency;

    private Integer cyclesNumber;
}
