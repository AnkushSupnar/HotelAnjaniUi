package com.anjani.controller.transaction;

import com.anjani.data.entity.Item;
import com.anjani.data.entity.Kirana;
import com.anjani.data.entity.KiranaTransaction;
import com.anjani.data.entity.PurchaseParty;
import com.anjani.data.service.CategoryService;
import com.anjani.data.service.ItemService;
import com.anjani.data.service.KiranaService;
import com.anjani.data.service.PurchasePartyService;
import com.anjani.print.PrintKiranaQuotation;
import com.anjani.view.AlertNotification;
import com.anjani.view.FxmlView;
import com.anjani.view.StageManager;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import impl.org.controlsfx.skin.AutoCompletePopup;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@Slf4j
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

    @FXML private TableView<Kirana> tableOldBill;
    @FXML private TableColumn<Kirana,Float> colBillAmount;
    @FXML private TableColumn<Kirana,Long> colBillNo;
    @FXML private TableColumn<Kirana, LocalDate> colDate;
    @FXML private TableColumn<Kirana,String> colPartyName;


    private ToggleGroup group;
    @FXML private MFXRadioButton rdbtnBill;
    @FXML private MFXRadioButton rdbtnQuotation;
    @FXML private TextField txtAmount,txtCategory;
    @FXML private MFXTextField txtDiscount,txtGrandTotal;
    @FXML private MFXTextField txtNetTotal,txtOther;
    @FXML private TextField txtParty,txtQty,txtRate,txtItemName;
    @FXML private MFXTextField txtTransporting;

    @FXML private TextField txtSearchBillNo;
    @FXML private TextField txtSearchParty;
    @FXML private DatePicker dateSearch;
    @FXML private Button btnViewAll;

    @Autowired private CategoryService categoryService;
    @Autowired private ItemService itemService;
    @Autowired private PurchasePartyService partyService;
    @Autowired private AlertNotification alert;
    @Autowired private KiranaService kiranaService;
    @Autowired private PrintKiranaQuotation printBill;
    private SuggestionProvider<String>categoryNames;
    private SuggestionProvider<String>itemNames;
    private SuggestionProvider<String>partyNames;
    private ObservableList<KiranaTransaction>trList = FXCollections.observableArrayList();
    private ObservableList<Kirana>billList = FXCollections.observableArrayList();
    private PurchaseParty party;
    private Item item;
    private Long id;
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate d;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        party = null;
        item=null;
        id=null;
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

        AutoCompletionBinding<String>searchPartyAuto = TextFields.bindAutoCompletion(txtSearchParty,partyNames);
        searchPartyAuto.prefWidthProperty().bind(txtSearchParty.widthProperty());
        searchPartyAuto.getAutoCompletionPopup().setStyle("-fx-font:25px 'kiran'");


        date.setValue(LocalDate.now());
        coAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        coSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemname"));
        colItemName.setCellFactory(new Callback<TableColumn<KiranaTransaction,String>, TableCell<KiranaTransaction,String>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<KiranaTransaction, String>()
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

        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        tableTr.setItems(trList);

        colBillAmount.setCellValueFactory(new PropertyValueFactory<>("grandtotal"));
        colBillAmount.setCellFactory(new Callback<TableColumn<Kirana,Float>, TableCell<Kirana,Float>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Kirana, Float>()
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
                            setFont(Font.font ("Arial", 16));
                            setText(item.toString());
                        }
                    }
                };
            }
        });
        colBillNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBillNo.setCellFactory(new Callback<TableColumn<Kirana,Long>, TableCell<Kirana,Long>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Kirana, Long>()
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
                            setFont(Font.font ("Arial", 16));
                            setText(item.toString());
                        }
                    }
                };
            }
        });
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setCellFactory(new Callback<TableColumn<Kirana,LocalDate>, TableCell<Kirana,LocalDate>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<Kirana, LocalDate>()
                {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if(isEmpty())
                        {
                            setText("");
                        }
                        else
                        {
                            setFont(Font.font ("Arial", 16));
                            setText(item.toString());
                        }
                    }
                };
            }
        });
        colPartyName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getParty().getName()));
        colPartyName.setCellFactory(new Callback<TableColumn<Kirana,String>, TableCell<Kirana,String>>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TableCell<KiranaTransaction, String>()
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
        billList.addAll(kiranaService.getByDate(LocalDate.now()));
        tableOldBill.setItems(billList);

        cmbUnit.getItems().add("ik.ga`a.");
        cmbUnit.getItems().add("naga");
        cmbUnit.setOnKeyPressed(e->{
            if(cmbUnit.getValue()!=null && e.getCode()== KeyCode.ENTER){
                txtQty.requestFocus();
            }
        });
        txtParty.setOnAction(e->{
            if(!txtParty.getText().isEmpty()){
                btnSearch.fire();
            }
        });
        btnSearch.setOnAction(e->{
            if(txtParty.getText().isEmpty()){
                alert.showError("Enter Party Name");
                txtParty.requestFocus();
            }
            else{
                party = partyService.getByName(txtParty.getText().trim());
                if(party==null){
                    alert.showError("No Party Found");

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
        cmbUnit.setOnAction(e->{
            if(cmbUnit.getValue()!=null){
                txtQty.requestFocus();
            }
        });
        txtQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,100}([\\.]\\d{0,4})?")) {
                    txtQty.setText(oldValue);
                }

            }
        });
        txtQty.setOnAction(e->{
            if(!txtQty.getText().isEmpty()){
                if(rdbtnQuotation.isSelected()) {
                    txtRate.setText("" + 0.0f);
                    calculateAmount();
                    btnAdd.requestFocus();
                }
                else
                    calculateAmount();

                txtRate.requestFocus();

            }
        });
        txtRate.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,100}([\\.]\\d{0,4})?")) {
                    txtRate.setText(oldValue);
                }
            }
        });
        txtRate.setOnAction(e->{
            if(rdbtnQuotation.isSelected()){
                txtRate.setText(""+0.0f);
                txtAmount.requestFocus();
            }else if(rdbtnBill.isSelected()){
                if (!txtRate.getText().isEmpty()) {
                    calculateAmount();
                    txtAmount.requestFocus();
                }
            }
            else{
                alert.showError("Select Bill Or Quotation");
            }
        });
        txtAmount.setOnAction(e->{
            if(rdbtnQuotation.isSelected()){
                txtAmount.setText(""+0.0f);
                btnAdd.requestFocus();
            }
            else{
                calculateAmount();
                if(txtAmount.getText().equals(""+0.0f) && rdbtnBill.isSelected()){
                    alert.showError("Enter Rate and Quantity again");
                    txtQty.requestFocus();
                }
                else{
                    btnAdd.requestFocus();
                }
            }
        });
        group = new ToggleGroup();
        txtSearchBillNo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,100}([\\.]\\d{0,4})?")) {
                    txtSearchBillNo.setText(oldValue);
                }

            }
        });
        txtSearchBillNo.setOnAction(e->{
            Kirana kirana=null;

            if(!txtSearchBillNo.getText().isEmpty() ){
                try {
                    kirana = kiranaService.getById(Long.parseLong(txtSearchBillNo.getText()));
                    billList.clear();
                    if (kirana != null)
                        billList.add(kiranaService.getById(Long.parseLong(txtSearchBillNo.getText())));
                    else alert.showError("No Bill found for this no");
                }catch (NumberFormatException ex){
                    alert.showError("No Bill found for this no");
                }
            }
        });
        txtSearchParty.setOnAction(e->{
            if(!txtSearchParty.getText().isEmpty()){
                billList.clear();
                billList.addAll(kiranaService.getByPartyName(txtSearchParty.getText()));
            }
        });
        dateSearch.setOnAction(e->{
            if(dateSearch.getValue()!=null){
                billList.clear();
                log.info("get By Date {}",dateSearch.getValue());
               billList.addAll(kiranaService.getByDate(dateSearch.getValue()));
            }
        });
        btnViewAll.setOnAction(e->{
            billList.clear();
            billList.addAll(kiranaService.getAllKirana());
        });
        rdbtnBill.setToggleGroup(group);
        rdbtnQuotation.setToggleGroup(group);
        btnAdd.setOnAction(e->add());
        btnRemove.setOnAction(e->remove());
        btnUpdate.setOnAction(e->update());
        btnClear.setOnAction(e->clear());
        btnSave.setOnAction(e->save());
        btnUpdatebill.setOnAction(e->updateBill());
        btnClearBill.setOnAction(e->clearBill());
        btnQuotation.setOnAction(e->print());
        btnHome.setOnAction(e->stageManager.switchScene(FxmlView.HOME));
    }

    private void print() {
        if(tableOldBill.getSelectionModel().getSelectedItem()==null)return;
        printBill.setKirana(kiranaService.getById(tableOldBill.getSelectionModel().getSelectedItem().getId()));
        printBill.print();
    }
    private void clearBill() {
        date.setValue(LocalDate.now());
        txtParty.setText("");
        //btnSearch.fire();
        btnSearch.setStyle("-fx-background-color:red;");
        trList.clear();
        txtNetTotal.setText(""+0.0f);
        txtOther.setText(""+0.0f);
        txtTransporting.setText(""+0.0f);
        txtDiscount.setText(""+0.0f);
        txtGrandTotal.setText(""+0.0f);
        rdbtnBill.setSelected(false);
        rdbtnBill.setDisable(false);
        rdbtnQuotation.setDisable(false);
        rdbtnQuotation.setSelected(false);
        clear();
        id = null;
    }
    private void updateBill(){
        if(tableOldBill.getSelectionModel().getSelectedItem()==null) return;
        Kirana kirana = kiranaService.getById(tableOldBill.getSelectionModel().getSelectedItem().getId());
        if(kirana!=null) {
            id = kirana.getId();
            date.setValue(kirana.getDate());
            trList.clear();

           // trList.addAll(kirana.getKiranaTransactions());
            Long i= Long.valueOf(0);
            for(KiranaTransaction tr: kirana.getKiranaTransactions()){
                tr.setId(++i);
                trList.add(tr);
            }
            txtNetTotal.setText(String.valueOf(kirana.getNettotal()));
            txtTransporting.setText(String.valueOf(kirana.getTransaport()));
            txtDiscount.setText(String.valueOf(kirana.getDiscount()));
            txtOther.setText(String.valueOf(kirana.getOther()));
            txtGrandTotal.setText(String.valueOf(kirana.getGrandtotal()));
            txtParty.setText(kirana.getParty().getName());
            btnSearch.fire();
            if (kirana.getGrandtotal() <= 0.0f) {
                rdbtnQuotation.setSelected(true);
            }
            else
                rdbtnBill.setSelected(true);
        }
    }
    private void save(){
        if(!validateBill())return;
        Kirana kirana =Kirana.builder()
                .date(date.getValue())
                .discount(Float.parseFloat(txtDiscount.getText()))
                .grandtotal(Float.parseFloat(txtGrandTotal.getText()))
                .party(party)
                .nettotal(Float.parseFloat(txtNetTotal.getText()))
                .other(Float.parseFloat(txtOther.getText()))
                .transaport(Float.parseFloat(txtTransporting.getText()))
                .kiranaTransactions(new ArrayList<>())
                .paid(0.0f)
                .build();
        if(id!=null){
            kirana.setId(id);
        }
        for(KiranaTransaction tr:trList){
            tr.setId(null);
            tr.setKirana(kirana);
            kirana.getKiranaTransactions().add(tr);
        }
      //  System.out.println("Save from client= "+kirana);
        System.out.println("Transactions=>");
        kirana.getKiranaTransactions().stream().forEach(e-> System.out.println(e.getKirana().getDate()));
        if(kiranaService.save(kirana)!=null){
            alert.showSuccess("Kirana Save Success");
            billList.clear();
            billList.addAll(kiranaService.getByDate(date.getValue()));
            clearBill();
        }

        //System.out.println(kirana);
    }
    private boolean validateBill() {
        if(trList.isEmpty()){
            alert.showError("No Data To Save");
            return false;
        }
        if(date.getValue()==null){
            alert.showError("Select Bill Date");
            return false;
        }
        if(party==null){
            alert.showError("Enter Party Name");
            txtParty.requestFocus();
            return false;
        }
        return true;
    }
    private void clear(){
        txtCategory.setText("");
        txtItemName.setText("");
        cmbUnit.getSelectionModel().clearSelection();
        txtRate.setText("");
        txtQty.setText("");
        txtAmount.setText("");
    }
    private void remove() {
        if(tableTr.getSelectionModel().getSelectedItem()==null) return;

        txtNetTotal.setText(String.valueOf(
                Float.parseFloat(txtNetTotal.getText())-
                tableTr.getSelectionModel().getSelectedItem().getAmount()));
        calculateGrandTotal();
        trList.remove(tableTr.getSelectionModel().getSelectedIndex());
        for(int i = 0;i<trList.size();i++){
            trList.get(i).setId((long) ++i);
        }
    }
    public void update(){
        if(tableTr.getSelectionModel().getSelectedItem()==null) return;
        txtItemName.setText(tableTr.getSelectionModel().getSelectedItem().getItemname());
        cmbUnit.setValue(tableTr.getSelectionModel().getSelectedItem().getUnit());
        txtQty.setText(String.valueOf(tableTr.getSelectionModel().getSelectedItem().getQuantity()));
        txtRate.setText(String.valueOf(tableTr.getSelectionModel().getSelectedItem().getRate()));
        calculateAmount();

        btnRemove.fire();
    }
    private void add() {
        if(!validate())return;
        System.out.println("btnAdd cllicked");
        KiranaTransaction tr = KiranaTransaction.builder()
                .amount(Float.parseFloat(txtAmount.getText()))
                .itemname(txtItemName.getText())
                .quantity(Float.parseFloat(txtQty.getText()))
                .rate(Float.parseFloat(txtRate.getText()))
                .unit(cmbUnit.getValue())
                .build();

        addInTrList(tr);
        if(id==null) {
            rdbtnQuotation.setDisable(true);
            rdbtnBill.setDisable(true);
        }
        else{
            rdbtnQuotation.setDisable(false);
            rdbtnBill.setDisable(false);
        }
        txtItemName.setText("");
        txtQty.setText("");
        txtRate.setText("");
        txtAmount.setText("");
    }
    private void addInTrList(KiranaTransaction tr) {
        if(trList.size()==0){
            tr.setId(1L);
            trList.add(tr);
            if(tr.getRate()>0.0f){
                rdbtnQuotation.setSelected(true);
            }
        }
        int index=-1;
        for(KiranaTransaction t:trList){
            if(t.getItemname().equals(tr.getItemname())){
                index = trList.indexOf(t);
                break;
            }
        }
        if(index==-1){
            tr.setId((long) (trList.size()+1));
            trList.add(tr);

        }
        else {
                trList.get(index).setQuantity(trList.get(index).getQuantity()+tr.getQuantity());
                trList.get(index).setAmount(trList.get(index).getAmount()+tr.getAmount());
                tableTr.refresh();
        }
        calculateGrandTotal();
        txtNetTotal.setText(
                String.valueOf(
                        Float.parseFloat(txtNetTotal.getText())+tr.getAmount()
                )
        );
        calculateGrandTotal();
    }
    void calculateGrandTotal(){
        if(txtNetTotal.getText().isEmpty())txtNetTotal.setText(""+0.0f);
        if(txtTransporting.getText().isEmpty())txtTransporting.setText(""+0.0f);
        if(txtOther.getText().isEmpty())txtOther.setText(""+0.0f);
        if(txtDiscount.getText().isEmpty())txtDiscount.setText(""+0.0f);
        txtGrandTotal.setText(
                String.valueOf(
                        Float.parseFloat(txtNetTotal.getText())+
                                Float.parseFloat(txtTransporting.getText())+
                                Float.parseFloat(txtOther.getText())-
                                Float.parseFloat(txtDiscount.getText())
                )
        );
    }
    void calculateAmount(){
        if(txtQty.getText().isEmpty()){txtQty.requestFocus();return;}
        if(txtRate.getText().isEmpty()){txtRate.requestFocus();return;}
        txtAmount.setText(
                String.valueOf(
                        Float.parseFloat(txtQty.getText())*Float.parseFloat(txtRate.getText())
                )
        );

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
        if(rdbtnBill.isSelected()){
            if(txtAmount.getText().equals(""+0.0f)){
                alert.showError("Enter Quantity and Rate again");
                txtQty.requestFocus();
                return false;
            }
        }
        return true;
    }
}

