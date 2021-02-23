package team16.literaryassociation.elasticsearch.dto.plagiator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaperDTO {

    private Long id;
    private String title;
    private MultipartFile file;
    private String pathForPDF;
    private double searchHits;
    private double similarProcent;
    private UserDTO user;
    private Long plagiatorId;
}
