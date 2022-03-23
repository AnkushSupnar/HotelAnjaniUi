package com.anjani.controller.transaction;

import com.anjani.config.SpringFXMLLoader;
import com.anjani.data.entity.TableMaster;
import com.anjani.data.service.TableGroupService;
import com.anjani.data.service.TableMasterService;
import com.anjani.view.StageManager;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import org.controlsfx.tools.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class BillingController implements Initializable {
    @Autowired @Lazy
    private StageManager stageManager;
    @Autowired
    private SpringFXMLLoader fxmlLoader;
    @FXML private SplitPane mainPane;
    @FXML private AnchorPane paneBilling;
    @FXML private AnchorPane paneItem;
    @FXML private AnchorPane paneTable;
    @FXML private VBox tableGroupBox;


    @Autowired private TableGroupService groupService;
    @Autowired private TableMasterService tableMasterService;
    private TableMaster table;
    private ObservableList<Button>tableButtonList = FXCollections.observableArrayList();
    FadeTransition ft;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addTableGroup();
        for(Button button:tableButtonList){
            if(button.getText().equalsIgnoreCase("A1"))
            {
                System.out.println(button.getText());
                button.setStyle("-fx-background-color:red");
                blink(button);
            }
        }
    }

    private void addTableGroup() {
        for(TableMaster table:tableMasterService.getAllTables()){
            Button button = new Button(table.getTablename());
            button.setAccessibleText(table.getTableGroup().getGroupname());
            button.setPrefWidth(60);
            button.getStyleClass().add("tablebutton");
            tableButtonList.add(button);
        }

        HBox hbox = new HBox();
        for(String group:groupService.getAllGroupNames()){
            TilePane tp = new TilePane();
            StackPane titlePane = new StackPane();
            titlePane.setStyle("-fx-background-color:#03DAC6");
            Label label = new Label(group);
            label.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.ITALIC, 16));
            label.setStyle("-fx-text-fill:white");
            titlePane.getChildren().add(label);

            tp.setHgap(5);
            tp.setVgap(5);
            tp.setStyle("-fx-border-color: white; -fx-padding: 5px; -fx-background-color:#03DAC6");
            //tp.setStyle("-fx-padding: 10px");
            if(group.equalsIgnoreCase("C") || group.equalsIgnoreCase("D")){
                hbox.getChildren().add(tp);
                if(group.equalsIgnoreCase("D"))
                tableGroupBox.getChildren().add(hbox);
            }else {
               tableGroupBox.getChildren().add(titlePane);
                tableGroupBox.getChildren().add(tp);
            }

            addTable(group,tp);
        }
    }

    private void addTable(String group, TilePane tp) {
        for(Button button:tableButtonList){
            if(group.equalsIgnoreCase(button.getAccessibleText())){
               // blink(button);
                tp.getChildren().add(button);
                //tp.getChildren().add(tableButton.get(tableButton.indexOf(bt)));
            }
        }

    }
    private void blink(Button button){
         ft = new FadeTransition(Duration.millis(500), button);

        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
       // System.out.println(ft.getNode());

        ft.play();
      //  ft.stop();
    }
    public void stopBlink(Button button){
       // FadeTransition f = button.get
       // button.defaultButtonProperty();

    }
}
