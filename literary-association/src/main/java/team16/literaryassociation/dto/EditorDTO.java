package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditorDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private String email;

    private String username;
}
