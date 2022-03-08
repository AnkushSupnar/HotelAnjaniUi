package com.anjani.data.service;

import com.anjani.data.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class LoginService {
     private RestTemplate template;

     @PostConstruct
     void init(){
         template = new RestTemplate();
     }
    public List<String> getAllName(){

        String url="http://localhost:8080/login/allusernames";
        String[] list =  template.getForObject(url,String[].class);
        return Arrays.asList(list);
    }
    public List<Login>getAllLogin(){
        String url="http://localhost:8080/login";
        Login[] list = template.getForObject(url,Login[].class);
        return Arrays.asList(list);
    }
    public List<String>getAllUserNames(){
        String url="http://localhost:8080/login/allusernames";
        String[] list = template.getForObject(url,String[].class);
        return Arrays.asList(list);
    }
    public Login validateLogin(String username,String password){
         String url="http://localhost:8080/login/validate/?username={username}&password={password}";
         Login l = template.getForObject(url,Login.class,username,password);
         return l;
    }
}
