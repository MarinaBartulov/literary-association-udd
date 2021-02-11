package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.User;
import team16.literaryassociation.model.VerificationToken;

public interface VerificationTokenService {

    VerificationToken findToken(String token);
    void createVerificationToken(User user, String token);
}
