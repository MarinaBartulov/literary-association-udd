package team16.literaryassociation.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchResultDTO {

    private Long id;
    private String title;
    private String writer;
    private String highlight;
    private String genre;
    private boolean openAccess;
}
