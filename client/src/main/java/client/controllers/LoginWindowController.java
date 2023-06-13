package client.controllers;

import client.App;
import client.Client;
import common.exceptions.ConnectionErrorException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginWindowController {

    private Client client;
    private App app;

    public void setClient(Client client) {
        this.client = client;
    }
    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfUsername;

    @FXML
    private Label warningsLabel;

    @FXML
    void bntLoginClicked(ActionEvent event) {
        try {
            if (client.processLogin(tfUsername.getText(), tfPassword.getText())) {
                try {
                    app.loadMainWindow();
                } catch (IOException e) {
                    System.exit(0);
                }
                //close this windol
            } else {
                tfUsername.clear();
                tfPassword.clear();
                warningsLabel.setText("видимо неверные пользовать и пароль но я хз");
            }
        } catch (ConnectionErrorException e) {
            warningsLabel.setText("Ошибка подключения к серверу");
        }
    }

    @FXML
    void btnRegisterClicked(ActionEvent event) {

    }

}
