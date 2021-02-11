package team16.literaryassociation.services.interfaces;

import team16.literaryassociation.model.User;

public interface UserService {

    User findByUsername(String username);
    void deleteUser(User user);
    User saveUser(User user);
    User findOne(Long userId);
    User findByEmail(String email);
}
