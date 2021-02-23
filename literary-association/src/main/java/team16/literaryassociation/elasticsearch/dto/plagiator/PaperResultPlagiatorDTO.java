package team16.literaryassociation.elasticsearch.dto.plagiator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaperResultPlagiatorDTO {

    private Long id;
    private List<ResultItemDTO> items = new ArrayList<ResultItemDTO>();
    private List<PaperDTO> similarPapers = new ArrayList<PaperDTO>();
    private PaperDTO uploadedPaper;
}
