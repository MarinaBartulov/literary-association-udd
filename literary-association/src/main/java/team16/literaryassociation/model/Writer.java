package team16.literaryassociation.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Writer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Writer extends User {

    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private VerificationToken token;

    @OneToOne(mappedBy = "writer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MembershipApplication membershipApplication;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "writer_genre",
            joinColumns = @JoinColumn(name = "writer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "writer")
    private Set<BookRequest> bookRequests = new HashSet<>();
}
