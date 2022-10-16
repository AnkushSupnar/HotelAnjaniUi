package com.anjani.controller.transaction;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.anjani.config.SpringFXMLLoader;
import com.anjani.controller.create.AddCustomerController;
import com.anjani.data.common.CommonData;
import com.anjani.data.common.Convertor;
import com.anjani.data.entity.*;
import com.anjani.data.service.*;
import com.anjani.print.PrintBill;
import com.anjani.print.PrintQt;
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
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
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
    @FXML private Button btnRunningTable,btnRemove,btnSave,btnSearchCustomer,btnShift,btnUpdate,btnUpdateBill,btnClose,btnExit;

    @FXML private Button btnAdd,btnOrder,btnAddCustomer,btnClear,btnClearBill,btnOldBillPrint,btnPrint;
    @FXML private ComboBox<String> cmbBankName;
    @FXML private TableView<TempTransaction> tableTransaction;
    @FXML private TableColumn<TempTransaction,String> colItemName;
    @FXML private TableColumn<TempTransaction,Float> colQuantity;
    @FXML private TableColumn<TempTransaction,Number> colRate;
    @FXML private TableColumn<TempTransaction,Long> colSrNo;
    @FXML private TableColumn<TempTransaction,Float> colAmount;

    @FXML private TableView<Bill> tableOldBill;
    @FXML private TableColumn<Bill,Number> colBillAmount;
    @FXML private TableColumn<Bill,Number> colBillNo;
    @FXML private TableColumn<Bill,String> colCash;
    @FXML private TableColumn<Bill,String> colCustomerName;
    @FXML private TableColumn<Bill,Number> colDiscount;
    @FXML private TableColumn<Bill,Number> colGrandTotal;
    @FXML private TableColumn<Bill,Number> colRecieved;
    @FXML private DatePicker date;
    @FXML private TextField txtAmount,txtCategory,txtCustomer,txtItem,txtCode;
    @FXML private TextField txtDiscount,txtGrand;
    @FXML private TextField txtNetTotal;
    @FXML private TextField txtOther;
    @FXML private TextField txtPhone,txtQuantity,txtRate;
    @FXML private TextField txtRecieved,txtRemaining;
    @FXML private ComboBox<String> cmbWaitor;
    @FXML
    private ListView<String> listCategory;
    @FXML
    private ListView<String>listItem;
    @FXML
    private ListView<Button> listQuantity;
    @FXML
    private TabPane tabpaneItem;
    @FXML
    private Tab tabCategory;
    @FXML
    private Tab tabQuantity;
    @FXML
    private Tab tabItem;
    @FXML
    private ComboBox<Integer> cmbPageSize;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrev;
    @FXML
    private Label lblPageNo;
    @Autowired private TableGroupService groupService;
    @Autowired private TableMasterService tableMasterService;
    @Autowired private ItemService itemService;
    @Autowired private CategoryService categoryService;
    @Autowired private EmployeeService employeeService;
    @Autowired private CustomerService customerService;
    @Autowired private TempTransactionService tempTransactionService;
    @Autowired private PrintQt printQt;


    @Autowired private AlertNotification alert;
    @Autowired private BankService bankService;
    @Autowired private BillService billService;
    @Autowired LoginService loginService;

    private TableMaster table;
    private ObservableList<Button>tableButtonList = FXCollections.observableArrayList();
    private SuggestionProvider<String>catNames;
    private ObservableList<String>catObsList = FXCollections.observableArrayList();
    private ObservableList<String>itemObsList = FXCollections.observableArrayList();
    private SuggestionProvider<String>itemNames;
    private SuggestionProvider<String>waitorNames;
    private SuggestionProvider<String>customerNames;
    private Item item;
    private Customer customer;
    private Employee waitor;
    FadeTransition ft;
    private ObservableList<TempTransaction>trList = FXCollections.observableArrayList();
    private ObservableList<Bill>oldBillList = FXCollections.observableArrayList();
    @Autowired
    PrintBill printBill;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        waitor = null;
        customer =null;
        date.setValue(LocalDate.now());
        addTableGroup();
        addAutoCompletion();
        setProperties();
        getOpenTable();
        addButtonToTablePlus();
        addButtonToTableMinus();
        loadCategoryList();


    }
    private void loadCategoryList(){
        catObsList.addAll(categoryService.getAllCategoryNames());
        listCategory.setItems(catObsList);
        listCategory.setOnMouseClicked(e->{
            listItem.getItems().clear();
            itemObsList.addAll(itemService.getItemNamesByCategoryName(listCategory.getSelectionModel().getSelectedItem()));
            listItem.setItems(itemObsList);
            listItem.requestFocus();
            SingleSelectionModel<Tab>selectionModel = tabpaneItem.getSelectionModel();
            selectionModel.select(tabItem);
        });
        listItem.setOnMouseClicked(e->{
            tabpaneItem.getSelectionModel().select(tabQuantity);
        });
        ObservableList<Button>qtyList = FXCollections.observableArrayList();
        for(int i=1;i<=10;i++){
            Button b = new Button();
            //b.setText(""+i);
            if(i==10)b.setText(""+0);
            else b.setText(""+i);
           b.setMaxHeight(70);
           b.setMaxWidth(100);
           b.setStyle("-fx-font-size:20");
            //b.setMinHeight(100);
            //b.setMaxWidth(100);
            qtyList.add(b);
            b.setOnAction(e->{
                if(listItem.getSelectionModel().getSelectedItem()==null){
                    tabpaneItem.getSelectionModel().select(tabQuantity);
                    if(itemObsList.isEmpty()){
                        tabpaneItem.getSelectionModel().select(tabCategory);
                    }
                    else{
                        tabpaneItem.getSelectionModel().select(tabItem);
                    }
                    return;
                }
                String item = listItem.getSelectionModel().getSelectedItem();
                setItem(itemService.getItemByName(item));
                txtQuantity.setText(""+Float.parseFloat(b.getText()));
                txtAmount.setText(String.valueOf(
                        Float.parseFloat(txtQuantity.getText())*Float.parseFloat(txtRate.getText()))
                );
                btnAdd.fire();
            });
        }
        listQuantity.setItems(qtyList);
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

        colItemName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getItemname()));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tableTransaction.setItems(trList);

        colBillAmount.setCellValueFactory(new PropertyValueFactory<>("netamount"));
        colBillNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCash.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getPaid()>0?"Cash":"Credit"));
        colCustomerName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getCustomer().getId()==1?"":cellData.getValue().getCustomer().getName()));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colGrandTotal.setCellValueFactory(new PropertyValueFactory<>("grandtotal"));
        colRecieved.setCellValueFactory(new PropertyValueFactory<>("paid"));
        loadOldBill(Integer.parseInt(lblPageNo.getText()),50);
        tableOldBill.setItems(oldBillList);


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
        btnOrder.setOnAction(e->order());

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
        txtRecieved.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,100}([\\.]\\d{0,4})?")) {
                    txtRecieved.setText(oldValue);
                }
            }
        });
        txtRecieved.setOnAction(e->{
            if(txtGrand.getText().isEmpty()){return;}
            txtRemaining.setText(
                    String.valueOf(
                            Float.parseFloat(txtRecieved.getText())-Float.parseFloat(txtGrand.getText())
                    )
            );
        });

        btnClose.setOnAction(e->closeBill());
        btnSave.setOnAction(e->saveBill());
        cmbPageSize.getItems().add(10);
        cmbPageSize.getItems().add(20);
        cmbPageSize.getItems().add(25);
        cmbPageSize.getItems().add(50);
        cmbPageSize.getItems().add(100);
        cmbPageSize.getSelectionModel().select(2);
        lblPageNo.setText("0");
        btnPrev.setOnAction(e->{
               if(!lblPageNo.getText().equals("0")) lblPageNo.setText(String.valueOf(Integer.parseInt(lblPageNo.getText())-1));
            loadOldBill(Integer.parseInt(lblPageNo.getText()),cmbPageSize.getValue());
        });
        btnNext.setOnAction(e->{
            lblPageNo.setText(""+(Integer.parseInt(lblPageNo.getText())+1));
            loadOldBill(Integer.parseInt(lblPageNo.getText()),cmbPageSize.getValue());
        });
        cmbPageSize.setOnAction(e->{
            loadOldBill(Integer.parseInt(lblPageNo.getText()),cmbPageSize.getValue());
        });
        btnUpdateBill.setOnAction(e->updateBill());
    }
    void loadOldBill(int offset,int pagesize){
        oldBillList.clear();
        oldBillList.addAll(billService.getBillWithPagination(offset,pagesize));
    }

    private void order() {
        if(table == null){
            alert.showError("Select Table First");
            return;
        }

        int flag =0;
        for(TempTransaction tr:trList){

            if(tr.getPrintqty()>0){
                flag=1;
               // return;
            }
        }
        List<TempTransaction>orderList =tempTransactionService.getOrder(table.getId());
        if(!orderList.isEmpty()){
            printQt.printQt(tempTransactionService.getOrder(table.getId()));
            tempTransactionService.resetPrintQuantity(table.getId());
        }
    }
    private void remove() {
        TempTransaction t = tableTransaction.getSelectionModel().getSelectedItem();
        if(t!=null){
            TempTransaction temp = tempTransactionService.getByItemAndTableAndRate(t.getItemname(),t.getTableMaster().getId(),t.getRate());
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
        System.out.println("GOt item in item=>"+itemService.getItemByName(t.getItemname()));
        setItem(itemService.getItemByName(t.getItemname()));
        txtQuantity.setText(String.valueOf(t.getQuantity()));
        txtRate.setText(String.valueOf(t.getRate()));
        txtAmount.setText(String.valueOf(t.getAmount()));
        waitor = t.getEmployee();
        cmbWaitor.getSelectionModel().select(waitor.getNickname());
        System.out.println("in update item");
        setItem(itemService.getItemByName(t.getItemname()));
    }
    private void clear() {
         item = null;
         setItem(null);

    }
    private void add() {

        if(!validate()) return;
        //check table is closed or not if first entry
       // item.setRate(Float.parseFloat(txtRate.getText()));
        System.out.println("in Add "+waitor);
        txtAmount.setText(String.valueOf(
                Float.parseFloat(txtQuantity.getText())*Float.parseFloat(txtRate.getText()))
        );
        TempTransaction tr = TempTransaction.builder()
                .amount(Float.parseFloat(txtAmount.getText()))
                .itemname(item.getItemname())
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
        System.out.println(tr);
        for(TempTransaction t:trList){
            System.out.println(t);
            if(t.getItemname().equals(tr.getItemname()) &&
            t.getTableMaster().getId().equals(tr.getTableMaster().getId()) &&
            t.getRate().equals(tr.getRate())){
                index = trList.indexOf(t);
                System.out.println("Got Index=====>"+index);
               // break;
            }
        }
        if(index==-1){//this is first entry
            tempTransactionService.save(tr);
            tr.setId((long) (trList.size()+1));
            trList.add(tr);
            getOpenTable();
        }
        else{
            TempTransaction temp = tempTransactionService.getByItemAndTableAndRate(tr.getItemname(),tr.getTableMaster().getId(),tr.getRate());

            if(temp!=null) {
                temp.setQuantity(temp.getQuantity() + tr.getQuantity());
               // System.out.println("Got tem="+temp.getQuantity()+" tr="+tr.getQuantity());
                temp.setAmount(temp.getAmount() + tr.getAmount());
                temp.setPrintqty(temp.getPrintqty() + tr.getPrintqty());
                tempTransactionService.save(temp);
                trList.get(index).setQuantity(trList.get(index).getQuantity() + tr.getQuantity());
                trList.get(index).setAmount(trList.get(index).getAmount() + tr.getAmount());
                tableTransaction.refresh();
            }
            else{
            tr.setId((long) (trList.size()+1));
            trList.add(tr);
            tempTransactionService.save(tr);
            }
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
        if(waitor.getId()==null){
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
            this.item = item;
        }
    }
    private void addAutoCompletion() {
        cmbWaitor.getItems().addAll(employeeService.getEmployeeNicknamesByDesignation("Waitor"));
        cmbBankName.getItems().addAll(bankService.getAllBankName());
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
           // button.setMaxHeight(100);
            button.setMinHeight(40);
            button.getStyleClass().add("tablebutton");
            tableButtonList.add(button);
        }
        HBox hbox = new HBox();
        //hbox.setStyle("-fx-background-color:#03DAC6");
        hbox.setStyle("-fx-background-color:#c8d9ed");
        for(String group:groupService.getAllGroupNames()){
            TilePane tp = new TilePane();
            StackPane titlePane = new StackPane();
            //titlePane.setStyle("-fx-background-color:#03DAC6");
            titlePane.setStyle("-fx-background-color:#71c7ec");;
            titlePane.setStyle("-fx-background-color:#c8d9ed");;
            Label label = new Label(group);
            label.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.ITALIC, 12));
            label.setStyle("-fx-text-fill:white");
            titlePane.getChildren().add(label);

            tp.setHgap(1.5);
            tp.setVgap(1.5);
            tp.setStyle("-fx-border-color: white; -fx-padding: 4px; -fx-background-color:#c8d9ed");
            //tp.setStyle("-fx-border-color: white; -fx-padding: 4px; -fx-background-color:#71c7ec");

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
        btnRunningTable.setText(button.getText());
        table = tableMasterService.getByName(button.getText());
        loadTableData(table);
    }
    private void loadTableData(TableMaster table){
        trList.clear();
        clearBill();
        cmbWaitor.getSelectionModel().clearSelection();
        trList.addAll(tempTransactionService.getByTableId(table.getId()));
        Long id=0L;
        //if(trList.size()==0){//get data from closed bill
            Bill bill = billService.getByTableNameAndStatus(table.getTablename(),"closed");
            if(bill!=null) {
                if (bill.getTransactions().size() != 0) {
                   // trList.clear();
                    id = (long) trList.size();
                    for (Transaction t : bill.getTransactions()) {
                        TempTransaction temp = new TempTransaction();
                        temp.setId(++id);
                        temp.setItemname(t.getItemname());
                        temp.setQuantity(t.getQuantity());
                        temp.setAmount(t.getQuantity() * t.getRate());
                        temp.setTableMaster(table);
                        temp.setPrintqty(0.0f);
                        temp.setRate(t.getRate());
                        temp.setEmployee(bill.getWaitor());
                       // tempTransactionService.save(temp);
                        trList.add(temp);
                    }
                }
            }
       // }else {
        id=0l;
            for (TempTransaction tr : trList) {
                trList.get(trList.indexOf(tr)).setId(++id);
                cmbWaitor.getSelectionModel().select(tr.getEmployee().getNickname());
            }
        //}
        if(!trList.isEmpty())
        waitor = employeeService.getByNickname(cmbWaitor.getValue());
        calculateNetTotal();
        tableTransaction.refresh();
        getOpenTable();
    }
    private void getOpenTable(){
        List<String> tableList = tempTransactionService.getOpenTableNames();
        List<String>closedTable = new ArrayList<>();
        for(Bill bill: billService.getByStatus("closed")){
            closedTable.add(bill.getTable().getTablename());
        }
        for(Button button:tableButtonList){
            if(tableList.contains(button.getText())){
                button.setStyle("-fx-background-color:#64DD17;-fx-border-color:#004D40;");
            }else
            if(closedTable.contains(button.getText())){
                button.setStyle("-fx-background-color:red;-fx-border-color:red;");
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
                button.setStyle("-fx-background-color:"+color);
               // blink(button);
            }
        }
    }
    private void closeBill() {
       if(!validateBill()) return;
        Bill bill = billService.getClosedBillByTableid(table.getId());
       //Bill bill = new Bill();
        List<Transaction> tr = new ArrayList<>();
        if(bill ==null){
            bill = new Bill();
        }
        else{

        }
        bill.setBank(bankService.getById(1L));
        bill.setCustomer(customer);
        bill.setDate(date.getValue());
        bill.setDiscount(Float.parseFloat(txtDiscount.getText()));
        bill.setGrandtotal(Float.parseFloat(txtGrand.getText()));
        bill.setNetamount(Float.parseFloat(txtNetTotal.getText()));
        bill.setPaid(0.0f);
        bill.setStatus("closed");
        bill.setWaitor(waitor);

            CommonData.login = loginService.validateLogin("Admin","123");
            System.out.println("Login from common==>"+CommonData.login);

        bill.setLogin(CommonData.login.getEmployee());
        bill.setTable(table);
        for(TempTransaction e:tempTransactionService.getByTableId(table.getId())){
            int index =findTempTransactionInTransaction(e,bill.getTransactions());

            if(index!=-1){
                Transaction t = bill.getTransactions().get(index);
                t.setQuantity(e.getQuantity()+t.getQuantity());
                bill.getTransactions().remove(index);
                bill.getTransactions().add(index,t);
            }else {
                Transaction t = new Transaction();
                t.setAmount(e.getAmount());
                t.setQuantity(e.getQuantity());
                t.setItemname(e.getItemname());
                t.setRate(e.getRate());
                t.setBill(bill);
               // tr.add(t);
                bill.getTransactions().add(t);
            }
        }
        //bill.setTransactions(tr);
       Bill  closed = billService.saveBill(bill);
        if(closed!=null){
            tempTransactionService.deleteByTable(bill.getTable());
            getOpenTable();

            tableTransaction.refresh();
            alert.showSuccess("Table Cosed");
        }
        else{
            alert.showError("Error in Table closing");
        }
    }
    private int findTempTransactionInTransaction(TempTransaction temp,List<Transaction>transList){
        for(Transaction tr:transList){
            if(tr.getItemname().equals(temp.getItemname()) && tr.getRate().equals(temp.getRate())){
                System.out.println("Found in transaction "+temp.getItemname());
               return transList.indexOf(tr);
            }
        }
        return -1;
    }
    private boolean validateBill(){
        if(btnRunningTable.getText().equalsIgnoreCase("table")){
            alert.showError("Select Table");
            return false;
        }
        if(trList.isEmpty()){
            alert.showError("No data to save");
            return false;
        }
        if(waitor==null){
            alert.showError("Select Waitor");
            return false;
        }
        if(customer==null){
            customer = customerService.getById(1L);
        }
        if(date.getValue()==null){
            date.setValue(LocalDate.now());
        }
        if(table==null){
            alert.showError("Select Opened Table");
            return false;
        }
        return true;
    }
    private void addButtonToTablePlus() {
        TableColumn<TempTransaction, Void> colBtn = new TableColumn("Add");

        Callback<TableColumn<TempTransaction, Void>, TableCell<TempTransaction, Void>> cellFactory = new Callback<TableColumn<TempTransaction, Void>, TableCell<TempTransaction, Void>>() {
            @Override
            public TableCell<TempTransaction, Void> call(final TableColumn<TempTransaction, Void> param) {
                final TableCell<TempTransaction, Void> cell = new TableCell<TempTransaction, Void>() {

                    private final Button btn = new Button("+1");
                    {
                        btn.setStyle("-fx-font: 18 Georgia; -fx-base: #5cb85c;");
                        btn.setOnAction((ActionEvent event) -> {
                            TempTransaction data = getTableView().getItems().get(getIndex());
                            tableTransaction.getSelectionModel().select(getIndex());
                            System.out.println("selectedData: " + data);
                            btnUpdate.fire();

                            txtQuantity.setText(""+1.0f);
                            System.out.println(item);
                            btnAdd.fire();
                            tableTransaction.getSelectionModel().clearSelection();
                        });
                    }


                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        tableTransaction.getColumns().add(colBtn);
    }
    private void addButtonToTableMinus() {
        TableColumn<TempTransaction, Void> colBtn = new TableColumn("Minus");

        Callback<TableColumn<TempTransaction, Void>, TableCell<TempTransaction, Void>> cellFactory = new Callback<TableColumn<TempTransaction, Void>, TableCell<TempTransaction, Void>>() {
            @Override
            public TableCell<TempTransaction, Void> call(final TableColumn<TempTransaction, Void> param) {
                final TableCell<TempTransaction, Void> cell = new TableCell<TempTransaction, Void>() {

                    private final Button btn = new Button("-1");
                    {
                      //  btn.setStyle("-fx-font-size:16");
                        btn.setStyle("-fx-font: 18 Georgia; -fx-base:#d9534f;");
                        btn.setMaxWidth(40);
                        btn.setMaxHeight(40);
                        btn.setOnAction((ActionEvent event) -> {
                            TempTransaction data = getTableView().getItems().get(getIndex());
                            tableTransaction.getSelectionModel().select(getIndex());
                            System.out.println("selectedData: " + data);
                            int index = -1;
                            List<TempTransaction>tempList = tempTransactionService.getByTableId(table.getId());
                            for(TempTransaction tr:tempList){
                                if(tr.getItemname().equals(data.getItemname())&& tr.getRate().equals(data.getRate())){
                                    index=tempList.indexOf(tr);
                                }
                            }
                            if(index==-1){
                                alert.showError("Item Can not be remove or reduce");
                            }else{
                                if(data.getQuantity()==1){
                                    remove();

                                }else {
                                   data.setQuantity(data.getQuantity()-1);
                                   data.setAmount(data.getQuantity()*data.getRate());
                                   update();
                                   txtQuantity.setText(""+data.getQuantity());

                                   remove();
                                   add();
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tableTransaction.getColumns().add(colBtn);

    }
    private void saveBill(){

        if(!validateSaveBill()){
            return;
        }
        Bill bill = billService.getClosedBillByTableid(table.getId());
        bill.setBank(bankService.getByName(cmbBankName.getValue()));
        if(customer ==null) {
            bill.setCustomer(customerService.getById(1L));
            customer = customerService.getById(1L);
        }
        else
        bill.setCustomer(customer);
        bill.setDate(date.getValue());
        bill.setDiscount(Float.parseFloat(txtDiscount.getText()));
        bill.setGrandtotal(Float.parseFloat(txtGrand.getText()));
        bill.setNetamount(Float.parseFloat(txtNetTotal.getText()));
        boolean result = true;

        if((Float.parseFloat(txtRecieved.getText())+Float.parseFloat(txtDiscount.getText()))<Float.parseFloat(txtGrand.getText()) && customer!=null)
        {
            result =  alert.showConfirmation("","Do You Want Add Remaining Amount in Credit?");
        }
        if(result)
            bill.setPaid(Float.parseFloat(txtRecieved.getText()));
        else
            return;
        bill.setStatus("paid");
        bill.setTable(table);

        if(billService.saveBill(bill)!=null){
            printBill.setBill(bill);
            printBill.print();
            getOpenTable();
            alert.showSuccess("Bill Saved Success");
            clearBill();
        }
        else {
            alert.showError("Bill Not Saved");
        }


    }
    private boolean validateSaveBill() {
        if(trList.size()==0){
            alert.showError("No Data To Save");
            return false;
        }
        if(date.getValue()==null){
            alert.showError("Select Date");
            return false;
        }
        if(tempTransactionService.getByTableId(tableMasterService.getByName(btnRunningTable.getText()).getId()).size()!=0)
        {
            alert.showError("Close Table First");
            return false;
        }
        if(cmbBankName.getValue()==null){
            alert.showError("Select Bank");
            return false;
        }
        if(txtRecieved.getText().isEmpty()){
            alert.showError("Enter Recieved Amount");
            txtRecieved.requestFocus();
            return false;
        }
        if(txtDiscount.getText().isEmpty()){
            txtDiscount.setText(""+0.0f);
        }
        if((Float.parseFloat(txtRecieved.getText())+Float.parseFloat(txtDiscount.getText()))<Float.parseFloat(txtGrand.getText()) && customer==null )
        {
           boolean result =  alert.showConfirmation("","Do You Want Add Remaining Amount in Discount?????");
            if(result){
                txtDiscount.setText(
                        String.valueOf((Float.parseFloat(txtGrand.getText())-Float.parseFloat(txtRecieved.getText()))));
                calculateGrandTotal();
                txtRemaining.setText(String.valueOf(Float.parseFloat(txtRecieved.getText())-Float.parseFloat(txtGrand.getText())));
                return true;
            }
            else
                return false;
        }
        return true;
    }
    private void clearBill()
    {
        trList.clear();
        txtNetTotal.setText(""+0.0f);
        txtDiscount.setText(""+0.0f);
        txtGrand.setText(""+0.0f);
        txtOther.setText(""+0.0f);
        txtRecieved.setText(""+0.0f);
        txtRemaining.setText("");
        customer = null;
        waitor = null;
        item = null;
        cmbBankName.getSelectionModel().clearSelection();
        cmbBankName.getSelectionModel().select(1);
        date.setValue(LocalDate.now());
    }
    private void updateBill(){
        if(tableOldBill.getSelectionModel().getSelectedItem()==null){
            return;
        }
        Bill bill = tableOldBill.getSelectionModel().getSelectedItem();
        bill = billService.getByBillno(bill.getId());


    }
}
