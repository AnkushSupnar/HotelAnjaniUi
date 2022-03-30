package com.anjani.controller.create;

import com.anjani.data.entity.Customer;
import com.anjani.data.service.CustomerService;
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
public class AddCustomerController implements Initializable {
    @FXML private Button btnAdd,btnClear,btnUpdate;
    @FXML private TableView<Customer> table;
    @FXML private TableColumn<Customer,String> colAddress;
    @FXML private TableColumn<Customer,String> colAlterMobile;
    @FXML private TableColumn<Customer,String> colContact;
    @FXML private TableColumn<Customer,Long> colId;
    @FXML private TableColumn<Customer,String> colName;
    @FXML private DialogPane dialog;
    @FXML private TextField txtAddress,txtAlterMobile,txtCustomerName,txtMobile;
    @Autowired private CustomerService customerService;
    @Autowired private AlertNotification alert;
    private Long id;
    private ObservableList<Customer>list = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        id=null;
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAlterMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        list.addAll(customerService.getAllCustomer());
        table.setItems(list);
        btnAdd.setOnAction(e->add());
        btnClear.setOnAction(e->clear());
        btnUpdate.setOnAction(e->update());
    }

    private void update() {
        if(table.getSelectionModel().getSelectedItem()==null)return;
        Customer customer = table.getSelectionModel().getSelectedItem();
        txtCustomerName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtAlterMobile.setText(customer.getMobile());
        txtMobile.setText(customer.getContact());
        id=customer.getId();
    }

    private void clear() {
        txtCustomerName.setText("");
        txtAddress.setText("");
        txtMobile.setText("");
        txtAlterMobile.setText("");
        id=null;
    }

    private void add() {
        if(!validate())return;
        Customer customer = Customer.builder()
                .address(txtAddress.getText())
                .contact(txtMobile.getText())
                .mobile(txtAlterMobile.getText())
                .name(txtCustomerName.getText())
                .build();
        if(id!=null){
            customer.setId(id);
        }

        if(customerService.save(customer)!=null){
            alert.showSuccess("Customer Saved Success");
            list.clear();
            list.addAll(customerService.getAllCustomer());
            btnClear.fire();
        }
    }

    private boolean validate() {
        if(txtCustomerName.getText().isEmpty()){
            alert.showError("Enter Customer Names");
            txtCustomerName.getText();
            return false;
        }
        if(txtAddress.getText().isEmpty()){
            alert.showError("Enter Address");
            txtAddress.requestFocus();
            return false;
        }
        if(txtMobile.getText().isEmpty()){
            alert.showError("Enter Contact No");
            txtMobile.requestFocus();
            return false;
        }
        if(txtAlterMobile.getText().isEmpty()){
            txtAlterMobile.setText(txtMobile.getText());
        }
        if(customerService.getByName(txtCustomerName.getText())!=null){
            alert.showError("This Name is already Registered");
            txtCustomerName.requestFocus();
            return false;
        }
        if(customerService.getByContact(txtMobile.getText())!=null){
            alert.showError("This Mobile No is already registered");
            txtMobile.requestFocus();
            return false;
        }
        return true;
    }
}
