package com.anjani.data.common;

import com.anjani.data.entity.Login;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.Properties;

@Component
public class CommonData {
    public static String url;//="http://localhost:8282/";
    public static Login login = new Login();
    public static void readFile()
    {
        try {
            FileReader reader = new FileReader("D:\\HotelAnjani\\hotel2022.properties");
            Properties prop = new Properties();
            prop.load(reader);
            url = prop.getProperty("url");
            System.out.println("Property Read Success "+url);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
