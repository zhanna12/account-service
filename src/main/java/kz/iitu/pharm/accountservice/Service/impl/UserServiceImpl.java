package kz.iitu.pharm.accountservice.Service.impl;

import kz.iitu.pharm.accountservice.Service.UserService;
import kz.iitu.pharm.accountservice.entity.User;
import kz.iitu.pharm.accountservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void createUser(User user) {

    }



    @Override
    public void updateUser(Long id, User user) {
        User userDb = userRepository.findById(id).orElse(null);

        if (userDb != null) {
            userDb.setUsername(user.getUsername());
            userDb.setPassword(user.getPassword()); // plaintext password

            userRepository.saveAndFlush(userDb);
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

}
