package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BookRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String synopsis;
    private boolean accepted;
    private String reasonForRejection;
    @ManyToOne
    private Genre genre;
    @ManyToOne
    private Writer writer;
    @ManyToOne
    private Editor editor;
    @OneToOne(mappedBy = "bookRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Manuscript manuscript;

}
