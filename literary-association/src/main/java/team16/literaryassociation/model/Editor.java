package team16.literaryassociation.model;

        import lombok.NoArgsConstructor;
        import team16.literaryassociation.dto.EditorDTO;

        import javax.persistence.*;
        import java.util.HashSet;
        import java.util.Set;

@Entity
@DiscriminatorValue("Editor")
@NoArgsConstructor
public class Editor extends User {

    @OneToMany(mappedBy = "editor")
    private Set<BookRequest> bookRequests;

    @OneToMany(mappedBy = "editor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EditorPlagiarismNote> editorPlagiarismNotes = new HashSet<>();

    public Editor(EditorDTO dto) {
        super(dto);
    }
}
