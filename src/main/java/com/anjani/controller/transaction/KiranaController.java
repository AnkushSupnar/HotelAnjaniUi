package com.anjani.controller.transaction;

import com.anjani.data.entity.Category;
import com.anjani.data.entity.Item;
import com.anjani.data.entity.KiranaTransaction;
import com.anjani.data.entity.PurchaseParty;
import com.anjani.data.service.CategoryService;
import com.anjani.data.service.ItemService;
import com.anjani.data.service.PurchasePartyService;
import com.anjani.view.AlertNotification;
import com.anjani.view.StageManager;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import impl.org.controlsfx.skin.AutoCompletePopup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class KiranaController implements Initializable {
    @Autowired
    @Lazy
    private StageManager stageManager;
    @FXML private AnchorPane mainPane;
    @FXML private Button btnSearch;
    @FXML private Button btnAdd,btnClear,btnClearBill,btnHome,btnQuotation,btnRemove,btnSave,btnUpdate,btnUpdatebill;
    @FXML private ComboBox<String> cmbUnit;
    @FXML private TableView<KiranaTransaction> tableTr;
    @FXML private TableColumn<KiranaTransaction, Float> coAmount;
    @FXML private TableColumn<KiranaTransaction, Long> coSrNo;
    @FXML private TableColumn<KiranaTransaction, String> colItemName;
    @FXML private TableColumn<KiranaTransaction, Float> colQty;
    @FXML private TableColumn<KiranaTransaction, Float> colRate;
    @FXML private TableColumn<KiranaTransaction,String> colUnit;
    @FXML private DatePicker date;
    private ButtonGroup group;
    @FXML private MFXRadioButton rdbtnBill;
    @FXML private MFXRadioButton rdbtnQuotation;
    @FXML private TextField txtAmount,txtCategory;
    @FXML private MFXTextField txtDiscount,txtGrandTotal;
    @FXML private MFXTextField txtNetTotal,txtOther;
    @FXML private TextField txtParty,txtQty,txtRate,txtItemName;
    @FXML private MFXTextField txtTransporting;

    @Autowired private CategoryService categoryService;
    @Autowired private ItemService itemService;
    @Autowired private PurchasePartyService partyService;
    @Autowired private AlertNotification alert;
    private SuggestionProvider<String>categoryNames;
    private SuggestionProvider<String>itemNames;
    private SuggestionProvider<String>partyNames;
    private ObservableList<KiranaTransaction>trList = FXCollections.observableArrayList();
    private PurchaseParty party;
    private Item item;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        party = null;
        item=null;
        categoryNames = SuggestionProvider.create(categoryService.getAllCategoryNames());
        AutoCompletionBinding<String> autoComplete = TextFields.bindAutoCompletion(txtCategory,categoryNames);
        autoComplete.prefWidthProperty().bind(this.txtCategory.widthProperty());
        AutoCompletePopup<String> autoCompletionPopup = autoComplete.getAutoCompletionPopup();
        autoCompletionPopup.setStyle("-fx-font:25px 'kiran'");

        itemNames = SuggestionProvider.create(itemService.getAllItemNames());
        //itemNames = SuggestionProvider.create(null);
        AutoCompletionBinding<String>itemAuto = TextFields.bindAutoCompletion(txtItemName,itemNames);
        itemAuto.prefWidthProperty().bind(txtItemName.widthProperty());
        itemAuto.getAutoCompletionPopup().setStyle("-fx-font:25px 'kiran'");

        partyNames = SuggestionProvider.create(partyService.getAllPartyNames());
        AutoCompletionBinding<String>partyAuto = TextFields.bindAutoCompletion(txtParty,partyNames);
        partyAuto.prefWidthProperty().bind(txtParty.widthProperty());
        partyAuto.getAutoCompletionPopup().setStyle("-fx-font:25px 'kiran'");

        coAmount.setCellValueFactory(new PropertyValueFactory<>(""));
        coSrNo.setCellValueFactory(new PropertyValueFactory<>(""));
        colItemName.setCellValueFactory(new PropertyValueFactory<>(""));
        colQty.setCellValueFactory(new PropertyValueFactory<>(""));
        colRate.setCellValueFactory(new PropertyValueFactory<>(""));
        colUnit.setCellValueFactory(new PropertyValueFactory<>(""));

        cmbUnit.getItems().add("KG");
        cmbUnit.getItems().add("NOS");
        btnSearch.setOnAction(e->{
            if(txtParty.getText().isEmpty()){
                alert.showError("Enter Party Name");
                txtParty.requestFocus();
            }
            else{
                party = partyService.getByName(txtParty.getText().trim());
                if(party==null){
                    btnSearch.setStyle("-fx-background-color:red;");
                }
                else{
                    btnSearch.setStyle("-fx-background-color:green;");
                }
            }
        });
        txtCategory.setOnAction(e->{
            if(!txtCategory.getText().isEmpty()){
                itemNames.clearSuggestions();
                itemNames.addPossibleSuggestions(itemService.getItemNamesByCategoryName(txtCategory.getText()));
                txtItemName.requestFocus();
            }
        });
        txtItemName.setOnAction(e->{
            if(!txtItemName.getText().isEmpty()){
                item = itemService.getItemByName(txtItemName.getText());
                txtCategory.setText(item.getCatid().getCategory());
                cmbUnit.requestFocus();
            }
        });

        rdbtnBill.setOnAction(e->{
            rdbtnQuotation.setSelected(false);
        });
        rdbtnQuotation.setOnAction(e->{
            rdbtnBill.setSelected(false);
        });
        btnAdd.setOnAction(e->add());
    }

    private void add() {
        if(!validate())return;
    }

    private boolean validate() {
        if(!rdbtnQuotation.isSelected() && !rdbtnBill.isSelected()){
            alert.showError("Select Bill or Quotation");
            return false;
        }
        if(txtItemName.getText().isEmpty()){
            alert.showError("Enter Item Name");
            txtItemName.requestFocus();
            txtItemName.requestFocus();
            return false;
        }
        if(cmbUnit.getValue()==null){
            alert.showError("Select Item Unit");
            cmbUnit.requestFocus();
            return false;
        }
        if(txtQty.getText().isEmpty()){
            alert.showError("Enter Item Quantity");
            txtQty.requestFocus();
            return false;
        }
        if(rdbtnBill.isSelected() && txtRate.getText().isEmpty()){
            alert.showError("Enter Item Rate");
            txtRate.requestFocus();
            return false;
        }
        if(rdbtnQuotation.isSelected()){
            txtRate.setText(""+0.0f);
            txtAmount.setText(""+0.0f);
        }
        return true;
    }
}

