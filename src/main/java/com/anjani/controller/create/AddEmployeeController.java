package com.anjani.controller.create;

import com.anjani.data.entity.Employee;
import com.anjani.data.entity.KiranaTransaction;
import com.anjani.data.service.EmployeeService;
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
import java.util.ResourceBundle;




@Component
public class AddEmployeeController implements Initializable {

    @FXML private Button btnAdd,btnUpdate,btnClear;
    @FXML private TableView<Employee> table;
    @FXML private TableColumn<Employee,String> colAddress;
    @FXML private TableColumn<Employee,String> colContact;
    @FXML private TableColumn<Employee,Long> colId;
    @FXML private TableColumn<Employee,String> colName;
    @FXML private TableColumn<Employee,String> colPost;
    @FXML private TableColumn<Employee,Float> colSalary;
    @FXML private TableColumn<Employee,Float> colSalryType;
    @FXML private DialogPane dialog;
    @FXML private ToggleGroup group;
    @FXML private RadioButton rdbtnActive,rdbtnInactive;

    @FXML private TextField txtAddress,txtDesignation,txtEmployeeName,txtMobile,txtNickName,txtSalary,txtSalaryType;

    @Autowired private EmployeeService employeeService;
    @Autowired private AlertNotification alert;
    private Long id;
    private ObservableList<Employee>list = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAddress.setCellFactory(new Callback<TableColumn<Employee,String>, TableCell<Employee,String>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Employee, String>()
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
                            setText(item);
                        }
                    }
                };
            }
        });
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colContact.setCellFactory(new Callback<TableColumn<Employee,String>, TableCell<Employee,String>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Employee, String>()
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
                            setText(item);
                        }
                    }
                };
            }
        });
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setCellFactory(new Callback<TableColumn<Employee,Long>, TableCell<Employee,Long>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Employee, Long>()
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
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setCellFactory(new Callback<TableColumn<Employee,String>, TableCell<Employee,String>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Employee, String>()
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
                            setText(item);
                        }
                    }
                };
            }
        });
        colPost.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colSalryType.setCellValueFactory(new PropertyValueFactory<>("salarytype"));
        list.clear();
        list.addAll(employeeService.getAllEmployee());
        table.setItems(list);
        btnAdd.setOnAction(e->save());
        btnUpdate.setOnAction(e->update());
        btnClear.setOnAction(e->clear());
    }

    private void clear() {
        txtDesignation.setText("");
        txtSalary.setText("");
        txtSalaryType.setText("");
        txtMobile.setText("");
        txtEmployeeName.setText("");
        txtNickName.setText("");
        txtAddress.setText("");
        rdbtnInactive.setSelected(false);
        rdbtnActive.setSelected(false);
        id=null;

    }

    private void update() {
        if(table.getSelectionModel().getSelectedItem()==null)return;
        Employee employee = table.getSelectionModel().getSelectedItem();
        txtDesignation.setText(employee.getDesignation());
        txtSalary.setText(String.valueOf(employee.getSalary()));
        txtSalaryType.setText(employee.getSalarytype());
        txtMobile.setText(employee.getContact());
        txtEmployeeName.setText(employee.getName());
        txtNickName.setText(employee.getNickname());
        txtAddress.setText(employee.getAddress());
        if (employee.getStatus().equalsIgnoreCase("Active")) {
            rdbtnActive.setSelected(true);
        } else {
            rdbtnInactive.setSelected(true);
        }
        id=employee.getId();

    }

    private void save() {
        if(!validate())return;
        Employee employee = Employee.builder()
                .address(txtAddress.getText().trim())
                .contact(txtMobile.getText().trim())
                .designation(txtDesignation.getText().trim())
                .name(txtEmployeeName.getText().trim())
                .nickname(txtNickName.getText().trim())
                .salary(Float.parseFloat(txtSalary.getText()))
                .salarytype(txtSalaryType.getText().trim())
                .status((rdbtnActive.isSelected()?"Active":"Inactive"))
                .build();
        System.out.println(employee);
        if(id!=null){
            employee.setId(id);
        }
    Employee flag = employeeService.save(employee);
        if(flag!=null){
            alert.showSuccess("Employee Saved Sucess");
            list.clear();
            list.addAll(employeeService.getAllEmployee());
            btnClear.fire();
        }

    }

    private boolean validate() {
        if(txtEmployeeName.getText().isEmpty()){
            alert.showError("Enter Employee Name");
            txtEmployeeName.requestFocus();
            return false;
        }
        if(txtNickName.getText().isEmpty()){
            alert.showError("Enter Employee Name to see on App");
            txtNickName.requestFocus();
            return false;
        }
        if(txtAddress.getText().isEmpty()){
            alert.showError("Enter Employee Address");
            txtAddress.requestFocus();
            return false;
        }
        if(txtMobile.getText().isEmpty()){
            alert.showError("Enter Employee Contact No");
            txtMobile.requestFocus();
            return false;
        }
        if(txtSalaryType.getText().isEmpty()){
            alert.showError("Enter Salary Type");
            txtSalaryType.requestFocus();
            return false;
        }
        if(txtSalary.getText().isEmpty()){
            alert.showError("Enter Salary ");
            txtSalary.requestFocus();
            return false;
        }
        if(txtDesignation.getText().isEmpty()){
            alert.showError("Enter Designation");
            txtDesignation.requestFocus();
            return false;
        }
        if(!rdbtnActive.isSelected() && !rdbtnInactive.isSelected()){
            alert.showError("Select Employee Active Situation");
            return false;
        }
        return true;
    }
}
