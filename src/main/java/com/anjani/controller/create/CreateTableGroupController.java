package com.anjani.controller.create;
import com.anjani.data.entity.TableGroup;
import com.anjani.data.service.TableGroupService;
import com.anjani.view.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class CreateTableGroupController implements Initializable {

    @FXML private Button btnAdd;
    @FXML private Button btnClear;
    @FXML private Button btnUpdate;
    @FXML private TableView<TableGroup> table;
    @FXML private TableColumn<TableGroup, Integer> colId;
    @FXML private TableColumn<TableGroup,String> colName;
    @FXML private DialogPane dialog;
    @FXML private TextField txtGroupName;

    @Autowired private TableGroupService service;
    @Autowired private AlertNotification alert;
    Integer id;
    private ObservableList<TableGroup>list = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id=null;
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("groupname"));
        list.addAll(service.getAll());
        table.setItems(list);
        btnAdd.setOnAction(e->{
            if(txtGroupName.getText().isEmpty()){
                alert.showError("Enter Group Name");
                txtGroupName.requestFocus();
                return;
            }
            if(service.getByName(txtGroupName.getText())!=null)
            {
                alert.showError("Table Group Name Already Exist !!! Enter Another Name");
                txtGroupName.requestFocus();
                return;
            }
            TableGroup group = TableGroup.builder()
                    .groupname(txtGroupName.getText())
                    .build();
            if(id!=null){
                group.setId(id);
            }
            TableGroup temp = service.save(group);
            if(temp!=null){
                alert.showSuccess("Table Group Name Saved Success");
                btnClear.fire();
                list.clear();
                list.addAll(service.getAll());
            }
        });
        btnUpdate.setOnAction(e->{
            if(table.getSelectionModel().getSelectedItem()==null) return;
            txtGroupName.setText(table.getSelectionModel().getSelectedItem().getGroupname());
            id = table.getSelectionModel().getSelectedItem().getId();
        });
        btnClear.setOnAction(e->{
            txtGroupName.setText("");
            id=null;
        });
    }
}