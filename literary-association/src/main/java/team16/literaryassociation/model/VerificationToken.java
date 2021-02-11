package team16.literaryassociation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private LocalDateTime timestamp;
    private String value;
    private long duration;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public VerificationToken(final String token, final User user) {
        this.value = token;
        this.user = user;
        this.timestamp = LocalDateTime.now();
        this.duration = 5; //to je 5 minuta. Zbog testiranja
    }

    public boolean isValid(){
        if(this.timestamp.plusMinutes(this.duration).isBefore(LocalDateTime.now())){
            return false;
        }else{
            return true;
        }
    }
}
