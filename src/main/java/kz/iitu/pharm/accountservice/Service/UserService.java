package kz.iitu.pharm.accountservice.Service;

import kz.iitu.pharm.accountservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUsers();
    void createUser(User user);
    void updateUser(Long id, User user);
}