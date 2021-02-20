package team16.literaryassociation.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchResultDTO {

    private Long id;
    private String title;
    private String writer;
    private String highlights;
    private String genre;
    private boolean openAccess;
    private String pdf;
}
