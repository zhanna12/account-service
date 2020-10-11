package kz.iitu.pharm.accountservice.Service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.iitu.pharm.accountservice.entity.Drug;
import kz.iitu.pharm.accountservice.repository.DrugRepository;
import kz.iitu.pharm.accountservice.Service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class DrugServiceImpl implements DrugService {
    @Autowired
    DrugRepository drugRepository;

    public List<Drug> getDrugs() throws IOException {
        String studentString = "http://localhost:8080/drugs/";
        String result = getResult(studentString);

        ObjectMapper mapper = new ObjectMapper();
        List<Drug> students = mapper.readValue(result, new TypeReference<List<Drug>>() {
        });
        return students;
    }

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

    @Override
    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    @Override
    public Optional<Drug> findById(Long id) {
        return drugRepository.findById(id);
    }

    @Override
    public Drug getDrug(Long drugId){
        return drugRepository.getOne(drugId);
    }

    @Override
    public Drug saveItem(Drug drug) {
        return drugRepository.save(drug);
    }


    @Transactional
    public boolean addDrug(Drug drugname) {
        if (drugRepository.findByName(drugname.getName()) != null) {
          //  drugname.setId(Long.MIN_VALUE);
            System.out.println("Error");
            return false;
        }
        drugRepository.save(drugname);
        return true;
    }
}
