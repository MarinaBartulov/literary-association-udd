package team16.literaryassociation.elasticsearch.dto.plagiator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResultItemDTO {

    private List<PaperDTO> papers;
    private int partOfPage;
    private String textToShow;
}
