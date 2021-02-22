package team16.literaryassociation.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.literaryassociation.elasticsearch.model.BetaReaderIndexUnit;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BRSearchResultDTO {

    private Long betaReaderId;
    private String fullName;
    private String genres;


    public BRSearchResultDTO(BetaReaderIndexUnit br){
        this.betaReaderId = br.getBetaReaderId();
        this.fullName = br.getFullName();
        this.genres = br.getGenres().replace(" ", ", ");
        this.genres = this.genres.substring(0, this.genres.length()-2);
    }
}
