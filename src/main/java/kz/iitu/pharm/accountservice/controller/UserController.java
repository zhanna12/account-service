package kz.iitu.pharm.accountservice.controller;

import kz.iitu.pharm.accountservice.Service.impl.UserServiceImpl;
import kz.iitu.pharm.accountservice.entity.Drug;
import kz.iitu.pharm.accountservice.entity.User;
import kz.iitu.pharm.accountservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Api(value = "User Controller class", description = "This class is used for accessing and editing user details")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RestTemplate restTemplate;

//    @ApiOperation(value = "Method to get list of users", response = List.class)
//    @GetMapping("")
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }

    @GetMapping("/")
    public List<User> getUsers() {
        return userRepository.findAll();
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

    @GetMapping("/list")
    public Drug[] getAllDrugs() {
        ResponseEntity<Drug[]> response =
                restTemplate.getForEntity(
                        "http://drug-service/drugs/",
                        Drug[].class);
        Drug[] products = response.getBody();

        return products;
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

    @GetMapping("/products/{customerId}")
    public List<Drug> requestAllProducts(@PathVariable Long customerId) {
        ResponseEntity<List<Drug>> responseEnties = null;
        List<Drug> response;

        Optional<User> customer = userRepository.findById(customerId);

        if (customer != null) {
            responseEnties = new RestTemplate().exchange(
                    "http://localhost:8080/drugs/",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Drug>>(){});
        }
        response = responseEnties.getBody();
        return response;
    }

    @GetMapping("/product/{productId}/customer/{customerId}")
    public Drug requestProductByProductId(@PathVariable Long productId,
                                                 @PathVariable Long customerId) {

        Drug response = new Drug();
        ResponseEntity<Drug> responseEntity;

        Optional<User> customer = userRepository.findById(customerId);

        if (customer != null) {
            Map<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("productId", productId);

            responseEntity = new RestTemplate().getForEntity(
                    "http://localhost:8080/drugs/id/{productId}",
                    Drug.class,
                    uriVariables);
            response = responseEntity.getBody();
        }
        return new Drug(response.getId(),
                response.getName(),
                response.getPrice());
    }

}