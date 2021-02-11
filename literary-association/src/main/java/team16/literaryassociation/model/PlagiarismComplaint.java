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
public class PlagiarismComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book myBook;

    @ManyToOne
    private Book bookPlagiat;

    @OneToMany(mappedBy = "plagiarismComplaint", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EditorPlagiarismNote> editorPlagiarismNotes = new HashSet<>();
}
