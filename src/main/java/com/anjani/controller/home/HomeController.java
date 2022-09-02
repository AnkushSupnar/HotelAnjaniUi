package com.anjani.controller.home;
import com.anjani.config.SpringFXMLLoader;
import com.anjani.controller.create.*;
import com.anjani.view.FxmlView;
import com.anjani.view.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class HomeController implements Initializable {
    @Autowired
    @Lazy
    private StageManager stageManager;
    @Autowired
    private SpringFXMLLoader fxmlLoader;
    @Autowired
    private SpringFXMLLoader loader;
    @FXML private BorderPane mainPane;
    @FXML private HBox menuBilling;
    @FXML private HBox menuKirana;
    @FXML private HBox menuNewTable;
    @FXML private HBox menuTableGroup;
    @FXML private HBox menuEmployee;
    @FXML private HBox menuAddCustomer;
    @FXML private HBox menuAddBank;


    private Pane center;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        menuBilling.setOnMouseClicked(e->{
            stageManager.switchScene(FxmlView.BILLING);
        });
        menuKirana.setOnMouseClicked(e->{
            stageManager.switchScene(FxmlView.KIRANA);
          //  center = stageManager.getFxmlLoader().getPage("/fxml/transaction/Kirana.fxml");
            //mainPane.setCenter(center);
        });
        menuTableGroup.setOnMouseClicked(e->{
            DialogPane pane =   fxmlLoader.getDialogPage("/fxml/create/CreateTableGroup.fxml");
            CreateTableGroupController dialog = fxmlLoader.getLoader().getController();
            Dialog<ButtonType> di = new Dialog<>();
            di.setDialogPane(pane);
            di.setTitle("Add New Table Group");
            di.setOnCloseRequest(e1->{
                System.out.println("closing");
                e1.consume();
            });
            Optional<ButtonType> clickedButton = di.showAndWait();
            if(clickedButton.isEmpty()){
                return;
            }
            if(clickedButton.get()==ButtonType.FINISH){
                System.out.println("Finished");
            }
        });
        menuNewTable.setOnMouseClicked(e->{
            DialogPane pane =   fxmlLoader.getDialogPage("/fxml/create/CreateTable.fxml");
            CreateTableController dialog = fxmlLoader.getLoader().getController();
            Dialog<ButtonType> di = new Dialog<>();
            di.setDialogPane(pane);
            di.setTitle("Add New Table Group");
            di.setOnCloseRequest(e1->{
                System.out.println("closing");
                e1.consume();
            });
            Optional<ButtonType> clickedButton = di.showAndWait();
            if(clickedButton.isEmpty()){
                return;
            }
            if(clickedButton.get()==ButtonType.FINISH){
                System.out.println("Finished");
            }
        });
        menuEmployee.setOnMouseClicked(e->{
            DialogPane pane =   fxmlLoader.getDialogPage("/fxml/create/CreateEmployee.fxml");
            AddEmployeeController dialog = fxmlLoader.getLoader().getController();
            Dialog<ButtonType> di = new Dialog<>();
            di.setDialogPane(pane);
            di.setTitle("Add New Employee");
            di.setOnCloseRequest(e1->{
                System.out.println("closing");
                //e1.consume();
            });
            Optional<ButtonType> clickedButton = di.showAndWait();
            if(clickedButton.isEmpty()){
                return;
            }
            if(clickedButton.get()==ButtonType.FINISH){
                System.out.println("Finished");
            }
        });
        menuAddCustomer.setOnMouseClicked(e->{
            DialogPane pane =   fxmlLoader.getDialogPage("/fxml/create/AddCustomer.fxml");
            AddCustomerController dialog = fxmlLoader.getLoader().getController();
            Dialog<ButtonType> di = new Dialog<>();
            di.setDialogPane(pane);
            di.setTitle("Add New Customer");
            di.setOnCloseRequest(e1->{
                System.out.println("closing");
                e1.consume();
            });
            Optional<ButtonType> clickedButton = di.showAndWait();
            if(clickedButton.isEmpty()){
                return;
            }
            if(clickedButton.get()==ButtonType.FINISH){
                System.out.println("Finished");
            }
        });
        menuAddBank.setOnMouseClicked(e->{
            DialogPane pane =   fxmlLoader.getDialogPage("/fxml/create/CreateBank.fxml");
            AddBankController dialog = fxmlLoader.getLoader().getController();
            Dialog<ButtonType> di = new Dialog<>();
            di.setDialogPane(pane);
            di.setTitle("Add New Bank");
            di.setOnCloseRequest(e1->{
                System.out.println("closing");
                e1.consume();
            });
            Optional<ButtonType> clickedButton = di.showAndWait();
            if(clickedButton.isEmpty()){
                return;
            }
            if(clickedButton.get()==ButtonType.FINISH){
                System.out.println("Finished");
            }
        });
    }
}
