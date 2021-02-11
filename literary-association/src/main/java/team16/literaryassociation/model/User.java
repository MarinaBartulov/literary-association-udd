package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team16.literaryassociation.dto.BoardMemberDTO;
import team16.literaryassociation.dto.EditorDTO;

import javax.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(nullable=false)
    private String firstName;
    @Column(nullable=false)
    private String lastName;
    @Column(nullable=false)
    private String city;
    @Column(nullable=false)
    private String country;
    @Column(nullable=false, unique = true)
    private String email;
    @Column(nullable=false, unique = true)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private boolean verified;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public User(BoardMemberDTO dto){
        this.id = dto.getId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.country = dto.getCountry();
        this.city = dto.getCity();
        this.email = dto.getEmail();
        this.username = dto.getUsername();
    }

    public User(EditorDTO dto){
        this.id = dto.getId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.country = dto.getCountry();
        this.city = dto.getCity();
        this.email = dto.getEmail();
        this.username = dto.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(!this.roles.isEmpty()){
            Role r = roles.iterator().next();
            List<Permission> permissions = new ArrayList<Permission>();
            for(Permission p : r.getPermission()){
                permissions.add(p);
            }
            return permissions;
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}