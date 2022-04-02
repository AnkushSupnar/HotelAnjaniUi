package com.anjani.controller.create;

import com.anjani.data.entity.Bank;
import com.anjani.data.service.BankService;
import com.anjani.view.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AddBankController implements Initializable {
    @FXML private Button btnAdd,btnClear,btnUpdate;
    @FXML private TableView<Bank> table;
    @FXML private TableColumn<Bank,String> colAccountno;
    @FXML private TableColumn<Bank,String> colAddress;
    @FXML private TableColumn<Bank,Float> colBalance;
    @FXML private TableColumn<Bank,Long> colId;
    @FXML private TableColumn<Bank,String> colIfsc;
    @FXML private TableColumn<Bank,String> colName;
    @FXML private DialogPane dialog;

    @FXML private TextField txtAccountNo,txtAddress,txtBalance,txtBankName,txtIfsc;

    @Autowired private BankService bankService;
    @Autowired private AlertNotification alert;
    private ObservableList<Bank>list = FXCollections.observableArrayList();
    Long id;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colAccountno.setCellValueFactory(new PropertyValueFactory<>("accountno"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIfsc.setCellValueFactory(new PropertyValueFactory<>("ifsc"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        list.addAll(bankService.getAll());

        btnAdd.setOnAction(e->save());

    }

    private void save() {
        if(!valid())return;
        Bank bank = Bank.builder()
                .accountno(txtAccountNo.getText())
                .address(txtAddress.getText())
                .balance(Float.parseFloat(txtBalance.getText()))
                .ifsc(txtIfsc.getText())
                .name(txtBankName.getText()).build();
        if(id!=null){
            bank.setId(id);
        }
    }

    private boolean valid() {
        if(txtBankName.getText().isEmpty()){
            alert.showError("Enter Bank Name");
            txtBankName.requestFocus();
            return false;
        }
        if(txtAddress.getText().isEmpty()){
            alert.showError("Enter Bank Address");
            txtAddress.requestFocus();
            return false;
        }
        if(txtIfsc.getText().isEmpty()){
            txtIfsc.setText("-");
        }
        if(txtAccountNo.getText().isEmpty()){
            alert.showError("Enter Account No");
            txtAccountNo.requestFocus();
            return false;
        }
        if(txtBalance.getText().isEmpty()){
            alert.showError("Enter Bank Balance");
            txtBalance.requestFocus();
            return false;
        }
        return true;
    }

}
