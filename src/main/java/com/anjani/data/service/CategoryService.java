package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
    private RestTemplate template;

    @Autowired
    public CategoryService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    public Category saveCategory(Category category){
        return template.postForObject(
                CommonData.url + "/category/save",category,Category.class);
    }
    public List<Category> getAllCategories(){
        return Arrays.asList(template.getForObject(CommonData.url+"/category/getall",Category[].class));
    }
    public List<String>getAllCategoryNames(){
        return Arrays.asList(template.getForObject(CommonData.url+"/category/allnames",String[].class));
    }
    public Category getById(Long id){
        return template.getForObject(CommonData.url+"/category/byid/{id}",Category.class,id);
    }


}
