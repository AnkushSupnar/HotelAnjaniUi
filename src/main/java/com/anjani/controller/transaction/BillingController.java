package com.anjani.controller.transaction;

import com.anjani.config.SpringFXMLLoader;
import com.anjani.controller.create.AddCustomerController;
import com.anjani.data.common.NotFoundException;
import com.anjani.data.entity.Customer;
import com.anjani.data.entity.Item;
import com.anjani.data.entity.TableMaster;
import com.anjani.data.entity.TempTransaction;
import com.anjani.data.service.*;
import com.anjani.view.AlertNotification;
import com.anjani.view.AutoCompleteTextField;
import com.anjani.view.StageManager;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import impl.org.controlsfx.skin.AutoCompletePopup;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleFloatProperty;
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

    @FXML private Button btnAdd,btnAddCustomer,btnClear,btnClearBill,btnOldBillPrint,btnPrint;
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
    FadeTransition ft;
    private ObservableList<TempTransaction>trList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTableGroup();
        addAutoCompletion();
        setProperties();
        for(Button button:tableButtonList){
            if(button.getText().equalsIgnoreCase("A1"))
            {
                System.out.println(button.getText());
                button.setStyle("-fx-background-color:red");
                blink(button);
            }
        }
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
        colRate.setCellValueFactory(cellData->new SimpleFloatProperty(cellData.getValue().getItem().getRate()));
        colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tableTransaction.setItems(trList);

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
    }

    private void add() {
        if(!validate()) return;
        TempTransaction tr = TempTransaction.builder()
                .amount(Float.parseFloat(txtAmount.getText()))
                .item(item)
                .printqty(Float.parseFloat(txtQuantity.getText()))
                .quantity(Float.parseFloat(txtQuantity.getText()))
                .build();
        System.out.println(tr);

    }

    private boolean validate() {
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
        txtItem.setText(item.getItemname());
        txtRate.setText(String.valueOf(item.getRate()));
        txtCode.setText(String.valueOf(item.getItemcode()));
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

    }
}
