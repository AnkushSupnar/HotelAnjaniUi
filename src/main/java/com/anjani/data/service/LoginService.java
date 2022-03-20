package com.anjani.data.service;

import com.anjani.data.common.CommonData;
import com.anjani.data.entity.Login;
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

        String url= CommonData.url+"/login/allusernames";
        String[] list =  template.getForObject(url,String[].class);
        return Arrays.asList(list);
    }
    public List<Login>getAllLogin(){
        String url=CommonData.url+"/login";
        Login[] list = template.getForObject(url,Login[].class);
        return Arrays.asList(list);
    }
    public List<String>getAllUserNames(){
        String url=CommonData.url+"/login/allusernames";
        String[] list = template.getForObject(url,String[].class);
        return Arrays.asList(list);
    }
    public Login validateLogin(String username,String password){
         String url=CommonData.url+"/login/validate/?username={username}&password={password}";
         Login l = template.getForObject(url,Login.class,username,password);
         return l;
    }
}
