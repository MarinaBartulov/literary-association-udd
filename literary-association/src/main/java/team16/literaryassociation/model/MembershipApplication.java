package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    private Boolean approved;

    @Column
    private Boolean approvedForPaying;

    @Column
    private Double price;

    @Column(nullable=false)
    private boolean paid;

    @Column
    private String processId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "writer_id", referencedColumnName = "id")
    private Writer writer;

    @OneToMany(mappedBy = "membershipApplication", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "membershipApplication", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BoardOpinion> boardOpinions = new HashSet<>();

    @OneToMany(mappedBy = "membershipApplication", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LiteraryWork> literaryWorks = new HashSet<>();

    public MembershipApplication(Writer writer, String processId){
        this.approved = false;
        this.approvedForPaying = null;
        this.processId = processId;
        this.price = 10.0;
        this.paid = false;
        this.writer = writer;
    }
}
