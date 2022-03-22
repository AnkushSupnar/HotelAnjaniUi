package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TableGroupService {
    private RestTemplate template;

    @Autowired
    public TableGroupService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public List<TableGroup> getAll(){
        return Arrays.asList(template.getForObject(CommonData.url+"/tablegroup/all",TableGroup[].class));
    }
    public TableGroup getById(Integer id){

        return template.getForObject(CommonData.url+"/tablegroup/byid/{id}",TableGroup.class,id);
    }
    public List<String>getAllGroupNames(){

        return Arrays.asList(template.getForObject(CommonData.url+"/tablegroup/allnames",String[].class));
    }
    public TableGroup getByName(String name){

        return template.getForObject(CommonData.url+"/tablegroup/byname/{name}",TableGroup.class,name);
    }
    public TableGroup save(TableGroup group){

        return template.postForObject(CommonData.url+"/tablegroup/save",group,TableGroup.class);
    }


}
