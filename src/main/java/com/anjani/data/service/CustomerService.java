package com.anjani.data.service;
import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomerService {
    private RestTemplate template;
    @Autowired
    public CustomerService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public List<Customer> getAllCustomer(){
       return Arrays.asList(template.getForObject(CommonData.url+"/customer/all",Customer[].class));
    }
    public Customer getByName(String name){
        return template.getForObject(CommonData.url+"/customer/byname/{name}",Customer.class,name);
    }
    public Customer getByContact(String contact){
        return template.getForObject(CommonData.url+"/customer/bycontact/{contact}",Customer.class,contact);
    }
    public Customer getByMobile(String mobile){
       return template.getForObject(CommonData.url+"/customer/bymobile/{mobile}",Customer.class,mobile);
    }
    public List<String>getAllNames(){
        return Arrays.asList(template.getForObject(CommonData.url+"/customer/allnames",String[].class));
    }
    public Customer save(Customer customer){
       return template.postForObject(CommonData.url+"/customer/save",customer,Customer.class);
    }
}
