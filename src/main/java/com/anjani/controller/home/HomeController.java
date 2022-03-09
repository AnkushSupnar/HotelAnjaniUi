package com.anjani.controller.home;

import com.anjani.view.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class HomeController implements Initializable {
    @Autowired
    @Lazy
    private StageManager stageManager;
    @FXML private BorderPane mainPane;
    @FXML private HBox menuBilling;
    @FXML private HBox menuKirana;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        menuBilling.setOnMouseClicked(e->{
            System.out.println("clicked");
        });
    }
}
