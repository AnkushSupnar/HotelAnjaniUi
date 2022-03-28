package com.anjani.controller.transaction;

import com.anjani.config.SpringFXMLLoader;
import com.anjani.data.entity.Item;
import com.anjani.data.entity.TableMaster;
import com.anjani.data.service.*;
import com.anjani.view.AutoCompleteTextField;
import com.anjani.view.StageManager;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import impl.org.controlsfx.skin.AutoCompletePopup;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.ResourceBundle;

@Component
public class BillingController implements Initializable {
    @Autowired @Lazy
    private StageManager stageManager;
    @Autowired
    private SpringFXMLLoader fxmlLoader;
    @FXML private SplitPane mainPane;
    @FXML private AnchorPane paneBilling;
    @FXML private AnchorPane paneItem;
    @FXML private AnchorPane paneTable;
    @FXML private VBox tableGroupBox;
    @FXML private Button btnRunningTable;

    @FXML private Button btnAdd;
    @FXML private Button btnAddCustomer;
    @FXML private Button btnClear;
    @FXML private Button btnClearBill;
    @FXML private Button btnOldBillPrint;
    @FXML private Button btnPrint;
    @FXML private Button btnRemove;
    @FXML private Button btnSave;
    @FXML private Button btnSearchCustomer;
    @FXML private Button btnShift;
    @FXML private Button btnUpdate;
    @FXML private Button btnUpdateBill;
    @FXML private ComboBox<?> cmbBankName;
    @FXML private TableColumn<?, ?> colAmount;
    @FXML private TableColumn<?, ?> colBillAmount;
    @FXML private TableColumn<?, ?> colBillNo;
    @FXML private TableColumn<?, ?> colCash;
    @FXML private TableColumn<?, ?> colCustomerName;
    @FXML private TableColumn<?, ?> colDiscount;
    @FXML private TableColumn<?, ?> colGrandTotal;
    @FXML private TableColumn<?, ?> colGrandTotal1;
    @FXML private TableColumn<?, ?> colItemName;
    @FXML private TableColumn<?, ?> colQuantity;
    @FXML private TableColumn<?, ?> colRate;
    @FXML private TableColumn<?, ?> colSrNo;
    @FXML private DatePicker date;


    @FXML private TableView<?> tableOldBill;
    @FXML private TableView<?> tableTransaction;
    @FXML private TextField txtAmount;
    @FXML private TextField txtCategory;
    @FXML private TextField txtCustomer;

    @FXML private MFXTextField txtDiscount;
    @FXML private MFXTextField txtGrand;
    @FXML private TextField txtItem,txtCode;
    @FXML private MFXTextField txtNetTotal;
    @FXML private MFXTextField txtOther;
    @FXML private TextField txtPhone,txtQuantity,txtRate,txtWaitor;
    @FXML private MFXTextField txtRecieved,txtRemaining;

    @Autowired private TableGroupService groupService;
    @Autowired private TableMasterService tableMasterService;
    @Autowired private ItemService itemService;
    @Autowired private CategoryService categoryService;
    @Autowired private EmployeeService employeeService;
    private TableMaster table;
    private ObservableList<Button>tableButtonList = FXCollections.observableArrayList();
    private SuggestionProvider<String>catNames;
    private SuggestionProvider<String>itemNames;
    private SuggestionProvider<String>waitorNames;
    private Item item;
    FadeTransition ft;
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

    private void setProperties() {
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
                item = itemService.getItemByCode(Integer.parseInt(txtCode.getText()));
                if(item!=null)setItem(item);
            }
        });


    }

    void setItem(Item item){
        txtItem.setText(item.getItemname());
        txtRate.setText(String.valueOf(item.getRate()));
    }
    private void addAutoCompletion() {
        waitorNames = SuggestionProvider.create(employeeService.getEmployeeNicknamesByDesignation("Waitor"));
        AutoCompletionBinding<String> waitorAuto = TextFields.bindAutoCompletion(txtWaitor,waitorNames);
        waitorAuto.prefWidthProperty().bind(this.txtCategory.widthProperty());
        AutoCompletePopup<String> waitorAutoCompletionPopup = waitorAuto.getAutoCompletionPopup();
        waitorAutoCompletionPopup.setStyle("-fx-font:25px 'kiran'");

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
