package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.common.RestTemplateResponseErrorHandler;
import com.anjani.data.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService {
    private RestTemplate template;
    @Autowired
    public EmployeeService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
    }
    public List<Employee> getAllEmployee(){
        return Arrays.asList(template.getForObject(CommonData.url+"/employee/all",Employee[].class));
    }
    public List<String>getEmployeeNamesByDesignation(String designation){
        return Arrays.asList(template.getForObject(CommonData.url+"/employee/allnamesbydesignation/{name}",String[].class,designation));
    }
    public List<String>getEmployeeNicknamesByDesignation(String designation){
        return Arrays.asList(template.getForObject(CommonData.url+"/employee/allnicknamesbydesignation/{name}",String[].class,designation));
    }
    public Employee getByName(String name){
        return template.getForObject(CommonData.url+"/byname/{name}",Employee.class,name);
    }
    public Employee save(Employee employee)
    {
        return template.postForObject(CommonData.url+"/employee/save",employee,Employee.class);
    }



}
