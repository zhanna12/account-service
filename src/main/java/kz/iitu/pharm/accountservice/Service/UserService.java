package kz.iitu.pharm.accountservice.Service;

import kz.iitu.pharm.accountservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    List<User> getAllUsers();
    void createUser(User user);
    void updateUser(Long id, User user);

    Optional<User> findById(Long UserId);
}