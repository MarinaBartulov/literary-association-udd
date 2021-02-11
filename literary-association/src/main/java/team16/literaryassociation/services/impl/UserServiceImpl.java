package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.model.User;
import team16.literaryassociation.repository.UserRepository;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(User user) {
        this.userRepository.delete(user);
    }

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User findOne(Long userId) {

        return userRepository.findById(userId).orElseGet(null);
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
