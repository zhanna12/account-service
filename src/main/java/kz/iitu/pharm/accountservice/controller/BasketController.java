package kz.iitu.pharm.accountservice.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import kz.iitu.pharm.accountservice.Service.impl.BasketServiceImpl;
import kz.iitu.pharm.accountservice.Service.impl.DrugServiceImpl;
import kz.iitu.pharm.accountservice.entity.Basket;
import kz.iitu.pharm.accountservice.repository.BasketRepository;
import kz.iitu.pharm.accountservice.repository.DrugRepository;
import kz.iitu.pharm.accountservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
//@RequestMapping("/baskets")
//@Api(value = "Basket Controller class", description = "This class is used for accessing, editing and deleting basket details")
public class BasketController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private BasketServiceImpl basketService;
    @Autowired
    private DrugServiceImpl drugService;

    public List<Basket> getBaskets() throws IOException {
        String baskets = "http://localhost:8081/basketservice/";
        String result = basketService.getResult(baskets);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result, new TypeReference<List<Basket>>() {
        });
    }




    @GetMapping("/{id}")
    @ResponseBody
    public Basket getBasketById(@PathVariable("id") Long id){
        return basketRepository.findById(id).get();
    }

    @ApiOperation(value = "Method for adding basket to user")
    @PatchMapping("/add/")
    public void addBasketToUser(@RequestParam("userId") Long userId, @RequestParam("basketId") Long basketId){
        if(basketService.addBasketToUser(userId,basketId)){
            System.out.println("Basket added to " + userId);
        }
        else{
            System.out.println("basket is already owned");
        }
    }
    @GetMapping("/shoppingCart")
    public ModelAndView shoppingCart() {
        ModelAndView modelAndView = new ModelAndView("/basket");
        modelAndView.addObject("drugs", basketService.getDrugsInBasket());
        modelAndView.addObject("total", basketService.getTotal().toString());
        return modelAndView;
    }
    @GetMapping("/shoppingCart/addProduct/{productId}")
    public ModelAndView addProductToCart(@PathVariable("productId") Long productId) {
        drugService.findById(productId).ifPresent(basketService::addDrug);
        return shoppingCart();
    }
    @GetMapping("/shoppingCart/removeProduct/{productId}")
    public ModelAndView removeProductFromCart(@PathVariable("productId") Long productId) {
        drugService.findById(productId).ifPresent(basketService::removeDrug);
        return shoppingCart();
    }
    @GetMapping("/shoppingCart/checkout")
    public ModelAndView checkout() {
        basketService.getTotal();
        return shoppingCart();
    }
    @ApiOperation(value = "Method for deleting basket")
    @DeleteMapping("/delete")
    public void clear(){
        basketService.clear();
    }
}
