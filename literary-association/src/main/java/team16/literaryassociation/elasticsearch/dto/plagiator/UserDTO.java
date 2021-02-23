package team16.literaryassociation.elasticsearch.dto.plagiator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private RoleDTO role;
}
