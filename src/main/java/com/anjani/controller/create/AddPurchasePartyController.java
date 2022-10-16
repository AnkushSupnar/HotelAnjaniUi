package com.anjani.controller.create;

import com.anjani.data.entity.Employee;
import com.anjani.data.entity.PurchaseParty;
import com.anjani.data.service.PurchasePartyService;
import com.anjani.view.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AddPurchasePartyController  implements Initializable {
    @FXML
    private Button btnClear;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private TableView<PurchaseParty> table;
    @FXML
    private TableColumn<PurchaseParty,String> colAddress;
    @FXML
    private TableColumn<PurchaseParty,String> colContact;

    @FXML
    private TableColumn<PurchaseParty,String> colEmail;

    @FXML
    private TableColumn<PurchaseParty,String> colGst;

    @FXML
    private TableColumn<PurchaseParty,String> colName;

    @FXML
    private TableColumn<PurchaseParty,String> colPan;

    @FXML
    private TableColumn<PurchaseParty,Integer> colSrNo;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtGst;
    @FXML
    private TextField txtMobile;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPan;

    @FXML
    private TextField txtddress;
    private ObservableList<PurchaseParty>partyList = FXCollections.observableArrayList();
    @Autowired
    private PurchasePartyService partyService;
    @Autowired private AlertNotification alert;
    private Integer id;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            id = 0;
            colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colAddress.setCellFactory(new Callback<TableColumn<PurchaseParty,String>, TableCell<PurchaseParty,String>>() {
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
            colContact.setCellFactory(new Callback<TableColumn<PurchaseParty,String>, TableCell<PurchaseParty,String>>() {
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
                                setFont(Font.font ("kiran", 20));
                                setText(item);
                            }
                        }
                    };
                }
            });
            colEmail .setCellValueFactory(new PropertyValueFactory<>("email"));
            colEmail.setCellFactory(new Callback<TableColumn<PurchaseParty,String>, TableCell<PurchaseParty,String>>() {
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
                            setFont(Font.font ("Verdana", 14));
                            setText(item);
                        }
                    }
                };
            }
        });
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colName.setCellFactory(new Callback<TableColumn<PurchaseParty,String>, TableCell<PurchaseParty,String>>() {
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
            colGst.setCellValueFactory(new PropertyValueFactory<>("gstno"));
            colGst.setCellFactory(new Callback<TableColumn<PurchaseParty,String>, TableCell<PurchaseParty,String>>() {
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
                            setFont(Font.font ("Verdana", 14));
                            setText(item);
                        }
                    }
                };
            }
        });
            colPan.setCellValueFactory(new PropertyValueFactory<>("pan"));
            colPan.setCellFactory(new Callback<TableColumn<PurchaseParty,String>, TableCell<PurchaseParty,String>>() {
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
                            setFont(Font.font ("Verdana", 14));
                            setText(item);
                        }
                    }
                };
            }
        });
            partyList.addAll(partyService.getAllPurchaseParty());
            table.setItems(partyList);

            btnClear.setOnAction(e->clear());
            btnSave.setOnAction(e->save());
            btnUpdate.setOnAction(e->update());

    }

    private void update() {
        if(table.getSelectionModel().getSelectedItem()==null) return;
        PurchaseParty party = table.getSelectionModel().getSelectedItem();
        txtddress.setText(party.getAddress());
        txtPan.setText(party.getPan());
        txtGst.setText(party.getGstno());
        txtMobile.setText(party.getContact());
        txtName.setText(party.getName());
        id = party.getId();

    }

    private void save() {
        if(!validate()){
            return;
        }
        PurchaseParty party = new PurchaseParty();
        party.setAddress(txtddress.getText());
        party.setContact(txtMobile.getText());
        party.setEmail(txtEmail.getText());
        party.setGstno(txtGst.getText());
        party.setName(txtName.getText());
        if(id!=0){
            party.setId(id);
        }
        System.out.println(party);
       PurchaseParty savedParty =  partyService.save(party);
       if(savedParty!=null){
           alert.showSuccess("Purchase Party Saved Success");
           System.out.println("saved party"+savedParty);
           partyList.clear();
           partyList.addAll(partyService.getAllPurchaseParty());
           clear();
       }
       else{
           alert.showError("Party Not Saved");
       }
    }

    private boolean validate() {
        try {
            if(txtName.getText().isEmpty()){
                alert.showError("Enter Party Name");
                return false;
            }
            if(txtddress.getText().isEmpty()){
                alert.showError("Enter Party Address");
                return false;
            }
            if(txtMobile.getText().isEmpty()){
                alert.showError("Enter Mobile No");
                return false;
            }
            if(txtEmail.getText().isEmpty())
            {
                txtEmail.setText("-");
            }
            if(txtGst.getText().isEmpty())
            {
                txtGst.setText("-");
            }
            if(txtPan.getText().isEmpty()){
                txtPan.setText("-");
            }

            return true;

        }catch(Exception e)
        {
            alert.showError("Error in Saving Party");
            return false;
        }
    }

    private void clear() {
        txtddress.setText("");
        txtMobile.setText("");
        txtName.setText("");
        txtGst.setText("");
        txtPan.setText("");
        id=0;
    }
}
