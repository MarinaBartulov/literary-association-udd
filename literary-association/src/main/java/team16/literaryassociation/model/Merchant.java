package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.literaryassociation.converter.SensitiveDataConverter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String merchantName;

    @Column(nullable = false, unique = true)
    private String merchantEmail;

    private boolean activated; //aktivira se kad zavrsi registraciju na PC

    @Column(name = "success_url")
    private String merchantSuccessUrl;

    @Column(name = "failed_url")
    private String merchantFailedUrl;

    @Column(name = "error_url")
    private String merchantErrorUrl;

    @OneToMany(mappedBy = "merchant")
    private Set<Order> orders = new HashSet<>();
}
