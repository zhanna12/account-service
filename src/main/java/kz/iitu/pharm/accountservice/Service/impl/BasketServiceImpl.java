package kz.iitu.pharm.accountservice.Service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.iitu.pharm.accountservice.entity.Basket;
import kz.iitu.pharm.accountservice.entity.Drug;
import kz.iitu.pharm.accountservice.repository.BasketRepository;
import kz.iitu.pharm.accountservice.repository.DrugRepository;
import kz.iitu.pharm.accountservice.repository.UserRepository;
import kz.iitu.pharm.accountservice.Service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BasketServiceImpl implements BasketService {
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DrugRepository drugRepository;
    @Autowired
    DrugServiceImpl drugService;

    private Map<Drug, Integer> drugs = new HashMap<>();


    public String getResult(String request) throws IOException {
        //Do the call
        URL url = new URL(request);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }


    public String getResultS(String request, String access_token) throws IOException {
        //Do the call
        URL url = new URL(request);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization","Bearer "+access_token);
        con.setRequestMethod("GET");
        con.connect();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    @Override
    public void addDrug(Drug drug) {
        if (drugs.containsKey(drug)) {
            drugs.replace(drug, drugs.get(drug) + 1);
        } else {
            drugs.put(drug, 1);
        }
    }

    @Override
    public void removeDrug(Drug drug) {
        if (drugs.containsKey(drug)) {
            if (drugs.get(drug) > 1)
                drugs.replace(drug, drugs.get(drug) - 1);
            else if (drugs.get(drug) == 1) {
                drugs.remove(drug);
            }
        }
    }

    @Transactional
    public Basket addDrugs(Long drugId){
        Basket basket = new Basket();
        Drug drug = drugRepository.findById(drugId).get();
        return basketRepository.save(basket);
    }

    @Transactional
    public boolean addBasketToUser(Long userId, Long basketId) {
        Basket basket = basketRepository.findById(basketId).get();
        basket.setUser(userRepository.findById(userId).get());
        basketRepository.save(basket);
        return true;
    }

    @Override
    public Map<Drug, Integer> getDrugsInBasket() {
        return  Collections.unmodifiableMap(drugs);
    }

    @Transactional
    public void save(Basket basket){
        basketRepository.save(basket);
    }

    @Transactional
    public void clear(){
        for(Basket b: basketRepository.findAll()){
            b.setUser(null);
            b.setId(null);
            basketRepository.save(b);
        }
        basketRepository.deleteAll();
    }

    @Override
    public BigDecimal getTotal() {
        return drugs.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public List<Basket> getBaskets(Long id) throws IOException {
        String studentString = "http://localhost:8081/baskets/id/" + id;
        String result = getResult(studentString);
        ObjectMapper mapper = new ObjectMapper();
        List<Basket> baskets = mapper.readValue(result, new TypeReference<List<Basket>>() {
        });
        return baskets;
    }

    @Override
    public void checkout() {
//        Product product;
//        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
//            // Refresh quantity for every product before checking
//            product = productRepository.findOne(entry.getKey().getId());
//            if (product.getQuantity() < entry.getValue())
//                throw new NotEnoughProductsInStockException(product);
//            entry.getKey().setQuantity(product.getQuantity() - entry.getValue());
//        }
//        productRepository.save(products.keySet());
//        productRepository.flush();
//        products.clear();
    }
}
