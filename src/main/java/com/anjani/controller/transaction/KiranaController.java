package com.anjani.controller.transaction;

import com.anjani.data.entity.Category;
import com.anjani.data.service.CategoryService;
import com.anjani.data.service.ItemService;
import com.anjani.view.StageManager;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import impl.org.controlsfx.skin.AutoCompletePopup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
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
    @FXML private Button btnAdd,btnClear,btnClearBill,btnHome,btnQuotation,btnRemove,btnSave,btnUpdate,btnUpdatebill;
    @FXML private ComboBox<?> cmbUnit;
    @FXML private TableView<?> tableTr;
    @FXML private TableColumn<?, ?> coAmount;
    @FXML private TableColumn<?, ?> coSrNo;
    @FXML private TableColumn<?, ?> colItemName;
    @FXML private TableColumn<?, ?> colQty;
    @FXML private TableColumn<?, ?> colRate;
    @FXML private TableColumn<?, ?> colUnit;
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
    private SuggestionProvider<String>categoryNames;
    private SuggestionProvider<String>itemNames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        categoryNames = SuggestionProvider.create(categoryService.getAllCategoryNames());
        AutoCompletionBinding<String> autoComplete = TextFields.bindAutoCompletion(txtCategory,categoryNames);
        autoComplete.prefWidthProperty().bind(this.txtCategory.widthProperty());
        AutoCompletePopup<String> autoCompletionPopup = autoComplete.getAutoCompletionPopup();
        autoCompletionPopup.setStyle("-fx-font:25px 'kiran'");

        itemNames = SuggestionProvider.create(itemService.getAllItemNames());
        AutoCompletionBinding<String>itemAuto = TextFields.bindAutoCompletion(txtItemName,itemNames);
        itemAuto.prefWidthProperty().bind(txtItemName.widthProperty());
        itemAuto.getAutoCompletionPopup().setStyle("-fx-font:25px 'kiran'");


    }
}
