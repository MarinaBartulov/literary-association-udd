package team16.literaryassociation.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchAdvancedDTO {

    private String field;
    private String operator;
    private String query;
    private boolean phrase;

}
