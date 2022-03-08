package com.anjani.config;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.scene.control.DialogPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
@Component
public class SpringFXMLLoader {
    private final ResourceBundle resourceBundle;
    private final ApplicationContext context;
    private FXMLLoader loader = new FXMLLoader();
    @Autowired
    public SpringFXMLLoader(ApplicationContext context, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        this.context = context;
    }
    public Parent load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setResources(resourceBundle);
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader.load();
    }

    public Pane getPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(context::getBean);
            loader.setResources(resourceBundle);
            loader.setLocation(getClass().getResource(fxmlPath));
           // this.loader = loader;
            return loader.load();
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
    public DialogPane getDialogPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(context::getBean);
            loader.setResources(resourceBundle);
            loader.setLocation(getClass().getResource(fxmlPath));
            this.loader = loader;

            return loader.load();
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public FXMLLoader getLoader() {
        return loader;
    }
}
