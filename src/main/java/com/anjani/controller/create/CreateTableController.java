package com.anjani.controller.create;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class CreateTableController implements Initializable {
    @FXML private Button btnAdd;
    @FXML private Button btnClear;
    @FXML private Button btnUpdate;
    @FXML private TableColumn<?, ?> colCharges;
    @FXML private TableColumn<?, ?> colGroup;
    @FXML private TableColumn<?, ?> colId;
    @FXML private TableColumn<?, ?> colName;
    @FXML private DialogPane dialog;
    @FXML private TextField txtCharges;
    @FXML private TextField txtGroupName;
    @FXML private TextField txtTableName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
