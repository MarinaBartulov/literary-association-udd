package team16.literaryassociation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenreDTO implements Serializable {

    private Long id;

    @NotNull(message = "Genre name cannot be null.")
    @NotBlank(message = "Genre name cannot be empty.")
    private String name;

    @NotNull(message = "Genre name cannot be null.")
    @NotBlank(message = "Genre name cannot be empty.")
    private String description;
}
