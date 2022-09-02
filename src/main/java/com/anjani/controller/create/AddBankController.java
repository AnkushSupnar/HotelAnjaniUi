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
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
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
        colAccountno.setCellFactory(new Callback<TableColumn<Bank,String>, TableCell<Bank,String>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Bank, String>()
                {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(isEmpty())
                        {
                            setText("");
                        }
                        else
                        {
                            setFont(Font.font ("kiran", 25));
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAddress.setCellFactory(new Callback<TableColumn<Bank,String>, TableCell<Bank,String>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Bank, String>()
                {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(isEmpty())
                        {
                            setText("");
                        }
                        else
                        {
                            setFont(Font.font ("kiran", 25));
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colBalance.setCellFactory(new Callback<TableColumn<Bank,Float>, TableCell<Bank,Float>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Bank, Float>()
                {
                    @Override
                    public void updateItem(Float item, boolean empty) {
                        super.updateItem(item, empty);
                        if(isEmpty())
                        {
                            setText("");
                        }
                        else
                        {
                            setFont(Font.font ("kiran", 25));
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setCellFactory(new Callback<TableColumn<Bank,Long>, TableCell<Bank,Long>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Bank, Long>()
                {
                    @Override
                    public void updateItem(Long item, boolean empty) {
                        super.updateItem(item, empty);
                        if(isEmpty())
                        {
                            setText("");
                        }
                        else
                        {
                            setFont(Font.font ("kiran", 25));
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });
        colIfsc.setCellValueFactory(new PropertyValueFactory<>("ifsc"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setCellFactory(new Callback<TableColumn<Bank,String>, TableCell<Bank,String>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Bank, String>()
                {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(isEmpty())
                        {
                            setText("");
                        }
                        else
                        {
                            setFont(Font.font ("kiran", 25));
                            setText(String.valueOf(item));
                        }
                    }
                };
            }
        });
        list.addAll(bankService.getAll());
        table.setItems(list);

        btnAdd.setOnAction(e->save());
        btnClear.setOnAction(e->clear());
        btnUpdate.setOnAction(e->update());

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
        if(bankService.save(bank)!=null){
            clear();
            loadAll();
            alert.showSuccess("Bank Saved Success");
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
    private void clear(){
        txtIfsc.setText("");
        txtBalance.setText("");
        txtAddress.setText("");
        txtBankName.setText("");
        txtAccountNo.setText("");
        id=null;
    }
    private void loadAll(){
        list.clear();
        list.addAll(bankService.getAll());
    }
    private void update(){
        if(table.getSelectionModel().getSelectedItem()==null) return;
        Bank bank = table.getSelectionModel().getSelectedItem();
        txtBankName.setText(bank.getName());
        txtAddress.setText(bank.getAddress());
        txtAccountNo.setText(bank.getAccountno());
        txtBalance.setText(""+bank.getBalance());
        txtIfsc.setText(bank.getIfsc());
        id=bank.getId();
    }
}
