package com.anjani.controller.transaction;

import com.anjani.config.SpringFXMLLoader;
import com.anjani.controller.create.AddCustomerController;
import com.anjani.data.entity.*;
import com.anjani.data.service.*;
import com.anjani.view.AlertNotification;
import com.anjani.view.StageManager;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import impl.org.controlsfx.skin.AutoCompletePopup;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class BillingController implements Initializable {
    @Autowired @Lazy
    private StageManager stageManager;
    @Autowired
    private SpringFXMLLoader fxmlLoader;
    @FXML private SplitPane mainPane;
    @FXML private AnchorPane paneBilling,paneItem,paneTable;
    @FXML private VBox tableGroupBox;
    @FXML private Button btnRunningTable,btnRemove,btnSave,btnSearchCustomer,btnShift,btnUpdate,btnUpdateBill;

    @FXML private Button btnAdd,btnOrder,btnAddCustomer,btnClear,btnClearBill,btnOldBillPrint,btnPrint;
    @FXML private ComboBox<String> cmbBankName;
    @FXML private TableView<TempTransaction> tableTransaction;
    @FXML private TableColumn<TempTransaction,String> colItemName;
    @FXML private TableColumn<TempTransaction,Float> colQuantity;
    @FXML private TableColumn<TempTransaction,Number> colRate;
    @FXML private TableColumn<TempTransaction,Long> colSrNo;
    @FXML private TableColumn<TempTransaction,Float> colAmount;

    @FXML private TableView<?> tableOldBill;
    @FXML private TableColumn<?, ?> colBillAmount;
    @FXML private TableColumn<?, ?> colBillNo;
    @FXML private TableColumn<?, ?> colCash;
    @FXML private TableColumn<?, ?> colCustomerName;
    @FXML private TableColumn<?, ?> colDiscount;
    @FXML private TableColumn<?, ?> colGrandTotal;
    @FXML private TableColumn<?, ?> colGrandTotal1;
    @FXML private DatePicker date;


    @FXML private TextField txtAmount,txtCategory,txtCustomer,txtItem,txtCode;
    @FXML private MFXTextField txtDiscount,txtGrand;
    @FXML private MFXTextField txtNetTotal;
    @FXML private MFXTextField txtOther;
    @FXML private TextField txtPhone,txtQuantity,txtRate;
    @FXML private MFXTextField txtRecieved,txtRemaining;
    @FXML private ComboBox<String> cmbWaitor;

    @Autowired private TableGroupService groupService;
    @Autowired private TableMasterService tableMasterService;
    @Autowired private ItemService itemService;
    @Autowired private CategoryService categoryService;
    @Autowired private EmployeeService employeeService;
    @Autowired private CustomerService customerService;
    @Autowired private TempTransactionService tempTransactionService;

    @Autowired private AlertNotification alert;
    private TableMaster table;
    private ObservableList<Button>tableButtonList = FXCollections.observableArrayList();
    private SuggestionProvider<String>catNames;
    private SuggestionProvider<String>itemNames;
    private SuggestionProvider<String>waitorNames;
    private SuggestionProvider<String>customerNames;
    private Item item;
    private Customer customer;
    private Employee waitor;
    FadeTransition ft;
    private ObservableList<TempTransaction>trList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addTableGroup();
        addAutoCompletion();
        setProperties();
        getOpenTable();

    }

    private void showAddCustomer() {
        DialogPane pane =   fxmlLoader.getDialogPage("/fxml/create/AddCustomer.fxml");
        AddCustomerController dialog = fxmlLoader.getLoader().getController();
        Dialog<ButtonType> di = new Dialog<>();
        di.setDialogPane(pane);
        di.setTitle("Add New Customer");
        di.setOnCloseRequest(e1->{
            e1.consume();
        });
        Optional<ButtonType> clickedButton = di.showAndWait();
        if(clickedButton.isEmpty()){
            customerNames.clearSuggestions();
            customerNames.addPossibleSuggestions(customerService.getAllNames());
            System.out.println("Closing");
            return;
        }
        if(clickedButton.get()==ButtonType.FINISH){

            customerNames.clearSuggestions();
            customerNames.addPossibleSuggestions(customerService.getAllNames());
            System.out.println("Finished");
        }
    }
    private void setProperties() {

        colItemName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getItem().getItemname()));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tableTransaction.setItems(trList);

        cmbWaitor.setOnAction(e->{
            if(cmbWaitor.getValue()!=null){
                waitor = employeeService.getByNickname(cmbWaitor.getValue());
                System.out.println(waitor);
            }
        });
        btnSearchCustomer.setOnAction(e->searchCustomer());
        btnAddCustomer.setOnAction(e->showAddCustomer());
        txtCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,100}([\\.]\\d{0,4})?")) {
                    txtCode.setText(oldValue);
                }
            }
        });
        txtCategory.setOnAction(e->{
            if(!txtCategory.getText().isEmpty()){
                itemNames.clearSuggestions();
                itemNames.addPossibleSuggestions(itemService.getItemNamesByCategoryName(txtCategory.getText()));
                txtCode.requestFocus();
            }
        });
        txtCode.setOnAction(e->{
            if(txtCode.getText().isEmpty()) txtItem.requestFocus();
            else{
                try {
                    item = itemService.getItemByCode(Integer.parseInt(txtCode.getText()));
                }catch(Exception ex){
                    System.out.println("Message== "+ex.getMessage());
                }
                if(item!=null)setItem(item);
                else {
                    txtItem.requestFocus();
                    txtCode.setText("");
                }
            }
        });
        txtItem.setOnAction(e->{
            if(txtItem.getText().isEmpty()) return;
            item = itemService.getItemByName(txtItem.getText());
            if(item!=null) {
                setItem(item);
                txtQuantity.requestFocus();
            }

        });
        txtQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,100}([\\.]\\d{0,4})?")) {
                    txtQuantity.setText(oldValue);
                }
            }
        });
        txtQuantity.setOnAction(e->{
            if(item==null){
                txtCategory.requestFocus();
                return;
            }
            if(!txtQuantity.getText().isEmpty()){
                if(!txtRate.getText().isEmpty()){
                    txtAmount.setText(
                            String.valueOf(Float.parseFloat(txtQuantity.getText())*Float.parseFloat(txtRate.getText()))
                    );
                    txtRate.requestFocus();
                }
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
            if(item==null){
                txtCategory.requestFocus();
                return;
            }
            if(!txtRate.getText().isEmpty()){
                if(!txtQuantity.getText().isEmpty()){
                    txtAmount.setText(
                            String.valueOf(Float.parseFloat(txtQuantity.getText())*Float.parseFloat(txtRate.getText()))
                    );
                    item.setRate(Float.parseFloat(txtRate.getText()));
                    txtAmount.requestFocus();
                }
                else txtQuantity.requestFocus();
            }
        });
        txtAmount.setOnAction(e->add());
        btnAdd.setOnAction(e->add());
        btnClear.setOnAction(e->clear());
        btnUpdate.setOnAction(e->update());
        btnRemove.setOnAction(e->remove());

        txtOther.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,100}([\\.]\\d{0,4})?")) {
                    txtOther.setText(oldValue);
                }
            }
        });
        txtOther.setOnAction(e->{
            calculateGrandTotal();
            txtDiscount.requestFocus();
        });
        txtDiscount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,100}([\\.]\\d{0,4})?")) {
                    txtDiscount.setText(oldValue);
                }
            }
        });
        txtDiscount.setOnAction(e->{
            calculateGrandTotal();
            txtGrand.requestFocus();
        });

    }

    private void remove() {
        TempTransaction t = tableTransaction.getSelectionModel().getSelectedItem();
        if(t!=null){
            TempTransaction temp = tempTransactionService.getByItemAndTableAndRate(t.getItem().getId(),t.getTableMaster().getId(),t.getRate());
            if(temp!=null){
                tempTransactionService.deleteById(temp.getId());
                loadTableData(t.getTableMaster());
                calculateNetTotal();
                //trList.remove(tableTransaction.getSelectionModel().getSelectedIndex());
            }
        }
        getOpenTable();
    }

    private void update() {
        if(tableTransaction.getSelectionModel().getSelectedItem()==null) return;
        TempTransaction t = tableTransaction.getSelectionModel().getSelectedItem();
        setItem(t.getItem());
        txtQuantity.setText(String.valueOf(t.getQuantity()));
        txtRate.setText(String.valueOf(t.getRate()));
        txtAmount.setText(String.valueOf(t.getAmount()));
        waitor = t.getEmployee();
        System.out.println(waitor);
        cmbWaitor.getSelectionModel().select(waitor.getNickname());


    }
    private void clear() {
         item = null;
         setItem(null);

    }
    private void add() {
        if(!validate()) return;
       // item.setRate(Float.parseFloat(txtRate.getText()));
        TempTransaction tr = TempTransaction.builder()
                .amount(Float.parseFloat(txtAmount.getText()))
                .item(item)
                .rate(Float.parseFloat(txtRate.getText()))
                .printqty(Float.parseFloat(txtQuantity.getText()))
                .quantity(Float.parseFloat(txtQuantity.getText()))
                .tableMaster(table)
                .employee(waitor)
                .build();
       // System.out.println(tr);
        addinTrTable(tr);
       btnClear.fire();


    }
    private void addinTrTable(TempTransaction tr) {
        int index=-1;
        for(TempTransaction t:trList){
            if(t.getItem().getItemname().equals(tr.getItem().getItemname()) &&
            t.getTableMaster().equals(tr.getTableMaster()) &&
            t.getRate().equals(tr.getRate())){
                index = trList.indexOf(t);
            }
        }
        if(index==-1){
            tempTransactionService.save(tr);
            tr.setId((long) (trList.size()+1));
            trList.add(tr);
            getOpenTable();
        }
        else{
            TempTransaction temp = tempTransactionService.getByItemAndTableAndRate(tr.getItem().getId(),tr.getTableMaster().getId(),tr.getRate());
            System.out.println("Old Temp==> "+temp);
            temp.setQuantity(temp.getQuantity()+tr.getQuantity());
            temp.setAmount(temp.getAmount()+tr.getAmount());
            temp.setPrintqty(temp.getPrintqty()+tr.getPrintqty());
            tempTransactionService.save(temp);
            trList.get(index).setQuantity(trList.get(index).getQuantity()+tr.getQuantity());
            trList.get(index).setAmount(trList.get(index).getAmount()+tr.getAmount());
            tableTransaction.refresh();
        }
        calculateNetTotal();
    }
    private void calculateNetTotal(){
        Float net=0.0f;
        for(TempTransaction tr:trList){
          net +=tr.getAmount();
        }
        txtNetTotal.setText(String.valueOf(net));
        calculateGrandTotal();
    }
    private void calculateGrandTotal(){
        if(txtNetTotal.getText().isEmpty())txtNetTotal.setText(""+0.0f);
        if(txtOther.getText().isEmpty())txtOther.setText(""+0.0f);
        if(txtDiscount.getText().isEmpty())txtDiscount.setText(""+0.0f);
        txtGrand.setText(
                String.valueOf(
                        Float.parseFloat(txtNetTotal.getText())+
                                Float.parseFloat(txtOther.getText())-
                                Float.parseFloat(txtDiscount.getText())
                )
        );
    }
    private boolean validate() {
        if(table==null){
            alert.showError("Select Table First");
            return false;
        }
        if(waitor==null){
            alert.showError("Select Waitor Name ");
            cmbWaitor.requestFocus();
            return false;
        }
        if(item==null){
            alert.showError("Select Item ");
            txtCategory.requestFocus();
            return false;
        }
        if(txtQuantity.getText().isEmpty()){
            alert.showError("Enter Quantity");
            txtQuantity.requestFocus();
            return false;
        }
        if(txtRate.getText().isEmpty()){
            txtRate.requestFocus();
            alert.showError("Enter Rate");
            return false;
        }
        if(txtAmount.getText().isEmpty() || txtAmount.getText().equalsIgnoreCase(""+0.0f)){
            alert.showError("Unable to add Item Check Rate and Quantity");
            txtQuantity.requestFocus();
            return false;
        }
        return true;
    }
    private void searchCustomer() {
        if(!txtCustomer.getText().isEmpty()){
            customer = customerService.getByName(txtCustomer.getText());
            if(customer!=null){
                btnSearchCustomer.setStyle("-fx-background-color:green");
                txtPhone.setText(customer.getContact());
            }
            else {
                btnSearchCustomer.setStyle("-fx-background-color:red");
                alert.showError("Customer Not Found");
                txtCustomer.requestFocus();
            }
            return;
        }
        if(!txtPhone.getText().isEmpty()){
            customer = customerService.getByContact(txtPhone.getText());
            if(customer!=null){
                btnSearchCustomer.setStyle("-fx-background-color:green");
                txtCustomer.setText(customer.getName());
            }
            else {
                btnSearchCustomer.setStyle("-fx-background-color:red");
                alert.showError("Customer Not Found");
                txtPhone.requestFocus();
            }
        }
        return;
    }
    void setItem(Item item){
        if(item==null){
            txtCategory.setText("");
            txtItem.setText("");
            txtQuantity.setText("");
            txtRate.setText("");
            txtAmount.setText("");
            txtCode.setText("");
        }
        else {
            txtItem.setText(item.getItemname());
            txtRate.setText(String.valueOf(item.getRate()));
            txtCode.setText(String.valueOf(item.getItemcode()));
            txtCategory.setText(item.getCatid().getCategory());
        }
    }
    private void addAutoCompletion() {
        cmbWaitor.getItems().addAll(employeeService.getEmployeeNicknamesByDesignation("Waitor"));
        catNames = SuggestionProvider.create(categoryService.getAllCategoryNames());
        AutoCompletionBinding<String> catAuto = TextFields.bindAutoCompletion(txtCategory,catNames);
        catAuto.prefWidthProperty().bind(this.txtCategory.widthProperty());
        AutoCompletePopup<String> autoCompletionPopup = catAuto.getAutoCompletionPopup();
        autoCompletionPopup.setStyle("-fx-font:25px 'kiran'");

        itemNames = SuggestionProvider.create(new ArrayList<>());
        AutoCompletionBinding<String> itemAuto = TextFields.bindAutoCompletion(txtItem,itemNames);
        itemAuto.prefWidthProperty().bind(this.txtItem.widthProperty());
        AutoCompletePopup<String> itemAutoCompletionPopup = itemAuto.getAutoCompletionPopup();
        itemAutoCompletionPopup.setStyle("-fx-font:25px 'kiran'");

        customerNames = SuggestionProvider.create(customerService.getAllNames());
        AutoCompletionBinding<String> customerAuto = TextFields.bindAutoCompletion(txtCustomer,customerNames);
        customerAuto.prefWidthProperty().bind(this.txtCustomer.widthProperty());
        AutoCompletePopup<String> customerAutoCompletionPopup = customerAuto.getAutoCompletionPopup();
        customerAutoCompletionPopup.setStyle("-fx-font:25px 'kiran'");

    }
    private void addTableGroup() {
        for(TableMaster table:tableMasterService.getAllTables()){
            Button button = new Button(table.getTablename());
            button.setAccessibleText(table.getTableGroup().getGroupname());
           // button.setPrefWidth(60);
            button.setOnAction(e->tableButtonAction(e));
            button.getStyleClass().add("tablebutton");
            tableButtonList.add(button);
        }
        HBox hbox = new HBox();
        hbox.setStyle("-fx-background-color:#03DAC6");
        for(String group:groupService.getAllGroupNames()){
            TilePane tp = new TilePane();
            StackPane titlePane = new StackPane();
            titlePane.setStyle("-fx-background-color:#03DAC6");
            Label label = new Label(group);
            label.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.ITALIC, 16));
            label.setStyle("-fx-text-fill:white");
            titlePane.getChildren().add(label);

            tp.setHgap(2);
            tp.setVgap(2);
            tp.setStyle("-fx-border-color: white; -fx-padding: 5px; -fx-background-color:#03DAC6");
            //tp.setStyle("-fx-padding: 10px");
            if(group.equalsIgnoreCase("C") || group.equalsIgnoreCase("D")){
                hbox.getChildren().add(tp);
                if(group.equalsIgnoreCase("D"))
                tableGroupBox.getChildren().add(hbox);
            }else {
               tableGroupBox.getChildren().add(titlePane);
                tableGroupBox.getChildren().add(tp);
            }

            addTable(group,tp);
        }
    }
    private void addTable(String group, TilePane tp) {
        for(Button button:tableButtonList){
            if(group.equalsIgnoreCase(button.getAccessibleText())){
                tp.getChildren().add(button);
            }
        }
    }
    private void blink(Button button){
         ft = new FadeTransition(Duration.millis(500), button);

        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
       // System.out.println(ft.getNode());

        ft.play();
      //  ft.stop();
    }
    public void stopBlink(Button button){
       // FadeTransition f = button.get
       // button.defaultButtonProperty();

    }
    public void tableButtonAction(ActionEvent e){
        Button button = (Button) e.getSource();
        System.out.println(button.getText());
        btnRunningTable.setText(button.getText());
        table = tableMasterService.getByName(button.getText());
        loadTableData(table);
        System.out.println(table);
    }
    private void loadTableData(TableMaster table){
        trList.clear();
        trList.addAll(tempTransactionService.getByTableId(table.getId()));
        Long id=0L;
        for(TempTransaction tr:trList){
            trList.get(trList.indexOf(tr)).setId(++id);
            cmbWaitor.getSelectionModel().select(tr.getEmployee().getNickname());
        }
        calculateNetTotal();
        tableTransaction.refresh();
        getOpenTable();
    }
    private void getOpenTable(){
        List<String> tableList = tempTransactionService.getOpenTableNames();
        for(Button button:tableButtonList){
            if(tableList.contains(button.getText())){
                button.setStyle("-fx-background-color:#64DD17;-fx-border-color:#004D40;");
            }
            else{
                button.setStyle("");
            }
        }
    }
    private void setColorTable(String table,String color){
        for(Button button:tableButtonList){
            if(button.getText().equalsIgnoreCase(table))
            {
                System.out.println(button.getText());
                button.setStyle("-fx-background-color:"+color);
               // blink(button);
            }
        }
    }
}
