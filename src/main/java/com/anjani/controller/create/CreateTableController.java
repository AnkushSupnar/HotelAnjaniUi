package com.anjani.controller.create;
import com.anjani.data.common.NotFoundException;
import com.anjani.data.entity.TableMaster;
import com.anjani.data.service.TableGroupService;
import com.anjani.data.service.TableMasterService;
import com.anjani.view.AlertNotification;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class CreateTableController implements Initializable {
    @FXML private Button btnAdd;
    @FXML private Button btnClear;
    @FXML private Button btnUpdate;
    @FXML private TableView<TableMaster> table;
    @FXML private TableColumn<TableMaster,Float> colCharges;
    @FXML private TableColumn<TableMaster,String> colGroup;
    @FXML private TableColumn<TableMaster,Integer> colId;
    @FXML private TableColumn<TableMaster,String> colName;
    @FXML private DialogPane dialog;
    @FXML private TextField txtCharges;
    @FXML private TextField txtGroupName;
    @FXML private TextField txtTableName;

    @Autowired private TableMasterService service;
    @Autowired private TableGroupService groupService;
    @Autowired AlertNotification alert;
    private ObservableList<TableMaster>list = FXCollections.observableArrayList();
    Integer id;
    private SuggestionProvider<String>groupNames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id=null;
        colCharges.setCellValueFactory(new PropertyValueFactory<>("othercharges"));
        colGroup.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getTableGroup().getGroupname()));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("tablename"));
        list.addAll(service.getAllTables());
        table.setItems(list);

        groupNames = SuggestionProvider.create(groupService.getAllGroupNames());
        new AutoCompletionTextFieldBinding<>(txtGroupName,groupNames);

        btnAdd.setOnAction(e->add());
        btnClear.setOnAction(e->id=null);
        btnUpdate.setOnAction(e->{
            if(table.getSelectionModel().getSelectedItem()==null)return;
            txtGroupName.setText(table.getSelectionModel().getSelectedItem().getTableGroup().getGroupname());
            txtTableName.setText(table.getSelectionModel().getSelectedItem().getTablename());
            txtCharges.setText(String.valueOf(table.getSelectionModel().getSelectedItem().getOthercharges()));
            id = table.getSelectionModel().getSelectedItem().getId();

        });
    }

    private void add() {
        if(!valid()) return;
        TableMaster table = TableMaster.builder()
                .tableGroup(groupService.getByName(txtGroupName.getText()))
                .tablename(txtTableName.getText())
                .othercharges(Float.parseFloat(txtCharges.getText()))
                .build();
        if(id!=null) table.setId(id);
        if(service.save(table)!=null){
            alert.showSuccess("Table Saved Success");
            btnClear.fire();
            list.clear();
            list.addAll(service.getAllTables());
        }
    }

    private boolean valid() {
        if(txtGroupName.getText().isEmpty()){
            alert.showError("Enter Table Group Name");
            txtGroupName.requestFocus();
            return false;
        }
        if(groupService.getByName(txtGroupName.getText())==null){
            alert.showError("Group Name Not Exist");
            txtGroupName.requestFocus();
            return false;
        }
        if(txtTableName.getText().isEmpty()){
            alert.showError("Enter Table Name");
            txtTableName.requestFocus();
            return false;
        }
//        if(service.getByTableNameAndGroupName(txtTableName.getText(),txtGroupName.getText())!=null) {
//            alert.showError("Table Name Already Exist in This Group");
//            txtTableName.requestFocus();
//            return false;
//        }
        for(TableMaster tb:list){
            if(tb.getTablename().equalsIgnoreCase(txtTableName.getText())&& tb.getTableGroup().getGroupname().equalsIgnoreCase(txtGroupName.getText()))
            {
                alert.showError("Table Name Already Exist in This Group");
                txtTableName.requestFocus();

                return false;
            }
        }
        if(txtCharges.getText().isEmpty()){
            txtCharges.setText(""+0.0f);
        }
        return true;
    }

}
