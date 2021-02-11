package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Reader")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reader extends User {

    private boolean betaReader;
    private int penaltyPoints;
    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private VerificationToken token;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "reader_genre",
            joinColumns = @JoinColumn(name = "reader_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "beta_reader_genre",
            joinColumns = @JoinColumn(name = "beta_reader_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> betaGenres = new HashSet<>();

    @OneToMany(mappedBy = "betaReader", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "reader", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    public Reader(Long id, String firstName, String lastName, String city, String country, String email,
                  String username, boolean betaReader, int penaltyPoints) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setCity(city);
        this.setCountry(country);
        this.setEmail(email);
        this.setUsername(username);
        this.betaReader = betaReader;
        this.penaltyPoints = penaltyPoints;
    }
}
