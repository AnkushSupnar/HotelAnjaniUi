package com.anjani.data.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

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
            System.out.println("Handle Client Error"+response.getStatusCode());
                if(response.getStatusCode()==HttpStatus.NOT_FOUND){
                    try {
                        throw new NotFoundException(""+response.getStatusCode());
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Not Found");
                }
        }

    }
}
