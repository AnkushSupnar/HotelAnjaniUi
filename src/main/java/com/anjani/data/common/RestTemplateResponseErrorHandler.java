package com.anjani.data.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.http.HttpResponse;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (
                response.getStatusCode().series()== HttpStatus.Series.CLIENT_ERROR ||
                        response.getStatusCode().series()== HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if(response.getStatusCode().series()==HttpStatus.Series.SERVER_ERROR){
            System.out.println("Server Error");
        }
        else if(response.getStatusCode().series()==HttpStatus.Series.CLIENT_ERROR){
            System.out.println("Handle Client Error");
                if(response.getStatusCode()==HttpStatus.NOT_FOUND){
                    //throw new NotFoundException();
                    System.out.println("Not Found");
                }
        }

    }
}
