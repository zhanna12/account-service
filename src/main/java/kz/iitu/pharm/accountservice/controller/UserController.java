package kz.iitu.pharm.accountservice.controller;

import kz.iitu.pharm.accountservice.Service.impl.UserServiceImpl;
import kz.iitu.pharm.accountservice.entity.User;
import kz.iitu.pharm.accountservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/users")
@Api(value = "User Controller class", description = "This class is used for accessing and editing user details")
public class UserController<BasketRepository> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private UserServiceImpl userService;

    @ApiOperation(value = "Method to get list of users", response = List.class)
    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/find/")
    public User getByName(@RequestParam("username") String username) {
        return userRepository.findByUsername(username);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id).get();
    }

    @ApiOperation(value = "Method for adding new users")
    @GetMapping("/create")
    public void createUserByUsernamePassword(String username,
                                             String password) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);

        userService.createUser(user);
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        System.out.println("UserController.createUser");
        System.out.println("user = " + user);

        userService.createUser(user);
    }

    @ApiOperation(value = "Update user by id")
    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id,
                           @RequestBody User user) {

        System.out.println("UserController.updateUser");
        System.out.println("id = " + id);
        System.out.println("User = " + user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication.getName() = " + authentication.getName());

        userService.updateUser(id, user);
    }
}