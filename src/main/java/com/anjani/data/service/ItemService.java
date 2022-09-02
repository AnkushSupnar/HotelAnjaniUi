package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemService {
    private RestTemplate template;

    @Autowired
    public ItemService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public Item getById(Long id){
        return template.getForObject(CommonData.url+"/item/byid/{id}",Item.class,id);
    }
    public List<Item> getAllItems(){
        return Arrays.asList(template.getForObject(CommonData.url+"/item/all",Item[].class));
    }
    public List<String>getAllItemNames(){
        return Arrays.asList(template.getForObject(CommonData.url+"/item/allnames",String[].class));
    }
    public Item getItemByName(String name){
        return template.getForObject(CommonData.url+"/item/byname/{name}",Item.class,name);
    }
    public List<Item>getItemsByCatId(Long catid){
        return Arrays.asList(template.getForObject(CommonData.url+"/item/bycatid/{catid}",Item[].class,catid));
    }
    public Item saveItem(Item item){
        return template.postForObject(CommonData.url+"/item/save",item,Item.class);
    }
    public List<String>getItemNamesByCategoryName(String catname){
        return Arrays.asList(
                template.getForObject(CommonData.url+"/item/bycategoryname?name="+catname,String[].class));
    }
    public Item getItemByCode(Integer code){
        Item item=null;
        try {
             item =  template.getForObject(CommonData.url + "/bycode/{code}",Item.class, code);
             if(item.getId()==null){
                 item=null;
             }
        }catch(HttpClientErrorException | HttpServerErrorException e){
            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                return null;
            }

        }
        return item;
    }

}
