package team16.literaryassociation.model;

import lombok.NoArgsConstructor;
import team16.literaryassociation.dto.BoardMemberDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@DiscriminatorValue("BoardMember")
public class BoardMember extends User {

    @OneToMany(mappedBy = "boardMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BoardOpinion> opinions = new HashSet<>();

    @OneToMany(mappedBy = "boardMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public BoardMember(BoardMemberDTO dto)
    {
        super(dto);
    }
}
