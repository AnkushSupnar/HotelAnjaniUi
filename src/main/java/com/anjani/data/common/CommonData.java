package com.anjani.data.common;

import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.Properties;

@Component
public class CommonData {
    public static String url="http://localhost:8080/";
    public static void readFile()
    {
        try {
            FileReader reader = new FileReader("D:\\HotelAnjani\\hotel2022.properties");
            Properties prop = new Properties();
            prop.load(reader);
            url = prop.getProperty("url");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
