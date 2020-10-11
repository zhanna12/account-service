package kz.iitu.pharm.accountservice.Service;

import kz.iitu.pharm.accountservice.entity.Basket;
import kz.iitu.pharm.accountservice.entity.Drug;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public interface BasketService {
    void addDrug(Drug drug);
    void removeDrug(Drug drug);
    boolean addBasketToUser(Long userId, Long BasketId);
    Map<Drug, Integer> getDrugsInBasket();
    BigDecimal getTotal();

    public List<Basket> getBaskets(Long id) throws IOException;
    void checkout();
}

