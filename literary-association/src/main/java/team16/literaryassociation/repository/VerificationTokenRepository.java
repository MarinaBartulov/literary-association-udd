package team16.literaryassociation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team16.literaryassociation.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByValue(String token);

}
