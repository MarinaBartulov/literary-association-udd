package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    private String content;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private MembershipApplication membershipApplication;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private BoardMember boardMember;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Manuscript manuscript;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Reader betaReader;
}
