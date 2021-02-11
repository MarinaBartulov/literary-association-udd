package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.literaryassociation.model.BoardMember;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardMemberDTO  implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private String email;

    private String username;

    public BoardMemberDTO(BoardMember boardMember){
        this.id = boardMember.getId();
        this.firstName = boardMember.getFirstName();
        this.lastName = boardMember.getLastName();
        this.city = boardMember.getCity();
        this.country = boardMember.getCountry();
        this.email = boardMember.getEmail();
        this.username = boardMember.getUsername();
    }
}
