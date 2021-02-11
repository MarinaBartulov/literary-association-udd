package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.enums.Opinion;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardOpinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Opinion opinion;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private MembershipApplication membershipApplication;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private BoardMember boardMember;
}
