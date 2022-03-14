package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.Kirana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class KiranaService {
    private RestTemplate template;

    @Autowired
    public KiranaService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public List<Kirana> getAllKirana()
    {
      return Arrays.asList(template.getForObject(CommonData.url+"/kirana/all",Kirana[].class));

    }
    public List<Kirana>getByDate(LocalDate date){
        return Arrays.asList(template.getForObject(CommonData.url+"/kirana/bydate/{date}",Kirana[].class,date));
    }
    public List<Kirana>getByPartyName(String party){
        return Arrays.asList(template.getForObject(CommonData.url+"/kirana/bypartyname/{party}",Kirana[].class,party));
    }
    public List<Kirana>getAllUnpaid(){
        return Arrays.asList(template.getForObject(CommonData.url+"/kirana/unpaid",Kirana[].class));
    }
    public Kirana getById(Long id){
        return template.getForObject(CommonData.url+"/kirana/byid/{id}",Kirana.class,id);
    }
    public Kirana save(Kirana kirana){
        return template.postForObject(CommonData.url+"/kirana/save",kirana,Kirana.class);
    }
}
