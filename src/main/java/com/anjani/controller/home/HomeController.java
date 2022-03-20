package com.anjani.controller.home;

import com.anjani.config.SpringFXMLLoader;
import com.anjani.view.FxmlView;
import com.anjani.view.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    @Autowired
    private SpringFXMLLoader loader;
    @FXML private BorderPane mainPane;
    @FXML private HBox menuBilling;
    @FXML private HBox menuKirana;

    private Pane center;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        menuBilling.setOnMouseClicked(e->{
            System.out.println("clicked");
        });
        menuKirana.setOnMouseClicked(e->{
            stageManager.switchScene(FxmlView.KIRANA);
          //  center = stageManager.getFxmlLoader().getPage("/fxml/transaction/Kirana.fxml");
            //mainPane.setCenter(center);
        });
    }
}
