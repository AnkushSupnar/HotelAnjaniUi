package com.anjani.controller;
import com.anjani.data.entity.Login;
import com.anjani.data.service.LoginService;
import com.anjani.view.AlertNotification;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {
    @FXML private Button btnCancel;
    @FXML private Button btnLogin;
    @FXML private ComboBox<String> cmbUserName;
    @FXML private AnchorPane mainPane;
    @FXML private MFXPasswordField txtPassword;


    @Autowired private LoginService loginService;
    @Autowired private AlertNotification alert;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cmbUserName.getItems().addAll(loginService.getAllUserNames());
        btnLogin.setOnAction(e->login());

    }

    private void login() {
        if(cmbUserName.getValue()==null){
            alert.showError("Enter User Name");
            cmbUserName.requestFocus();
            return;
        }
        if(txtPassword.getText().isEmpty()){
            alert.showError("Enter your Password");
            txtPassword.requestFocus();
            return;
        }
        Login login = loginService.validateLogin(cmbUserName.getValue(),txtPassword.getText());
        if(login!=null){
            alert.showSuccess("Wel come "+cmbUserName.getValue());
        }
        else{
            alert.showError("Enter Correct Password");
            txtPassword.requestFocus();
        }
        System.out.println(login);
    }
}
