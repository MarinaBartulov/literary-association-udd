package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Manuscript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pdf; //putanja do pdf fajla gde je sacuvan, ona ce da se menja sa objavljivanjem novih verzija
    private boolean original;
    private boolean accepted;
    private boolean finalEditorsApproval;
    private String suggestions;
    private String reasonForRejection;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_request_id")
    private BookRequest bookRequest;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "manuscript_beta_reader",
            joinColumns = @JoinColumn(name = "manuscript_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "beta_reader_id", referencedColumnName = "id"))
    private Set<Reader> betaReaders = new HashSet<>();

    @OneToMany(mappedBy = "manuscript", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Lecturer lecturer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
