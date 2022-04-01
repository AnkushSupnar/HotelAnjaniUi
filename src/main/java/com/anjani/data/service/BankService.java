package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class BankService {
    private RestTemplate template;
    @Autowired
    public BankService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
        CommonData.readFile();
    }
    public List<String> getAllBankName(){
        return Arrays.asList(template.getForObject(CommonData.url+"/bank/allname",String[].class));
    }
    public Bank getByName(String name){
        return template.getForObject(CommonData.url+"/bank/byname/{name}",Bank.class,name);
    }
    public Bank getById(Long id){
        return template.getForObject(CommonData.url+"/bank/byid/{id}",Bank.class,id);
    }
    public Bank save(Bank bank){
        return template.postForObject(CommonData.url+"/bank/save",bank,Bank.class);
    }

}
