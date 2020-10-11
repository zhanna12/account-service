package kz.iitu.pharm.accountservice.Service;

import kz.iitu.pharm.accountservice.entity.Drug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DrugService {
    @Autowired
    List<Drug> getAllDrugs();
    Optional<Drug> findById(Long id);

    Drug getDrug(Long grudId);

    Drug saveItem(Drug drug);
}
