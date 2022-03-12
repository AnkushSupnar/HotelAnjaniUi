package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.PurchaseParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class PurchasePartyService {
    private RestTemplate template;

    @Autowired
    public PurchasePartyService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public List<PurchaseParty> getAllPurchaseParty(){
        return Arrays.asList(
                template.getForObject(CommonData.url+"/purchaseparty/getall",PurchaseParty[].class));
    }
    public List<String>getAllPartyNames(){
        return Arrays.asList(
                template.getForObject(CommonData.url+"/purchaseparty/allname",String[].class));
    }
    public PurchaseParty getById(Integer id){
        return template.getForObject(CommonData.url+"/purchaseparty/byid/{id}",PurchaseParty.class,id);

    }
    public PurchaseParty getByName(String name){
        return template.getForObject(CommonData.url+"/purchaseparty/byname/{name}",PurchaseParty.class,name);
    }
    public PurchaseParty save(PurchaseParty party){
        return template.postForObject(CommonData.url+"/purchaseparty/save",party,PurchaseParty.class);
    }

}
