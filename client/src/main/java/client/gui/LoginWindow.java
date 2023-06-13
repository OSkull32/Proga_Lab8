package client.gui;

import client.App;
import client.Client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


public class LoginWindow {
    private final Color CONNECTED_COLOR = Color.GREEN;
    private final Color NOT_CONNECTED_COLOR = Color.RED;
    App app;
    Client client;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private CheckBox registerCheckBox;
    @FXML
    private Label isConnectedLabel;
    @FXML
    private Button signInButton;
    private ResourceFactory resourceFactory;

    @FXML
    private void signInButtonOnAction() {
        if (client.processAuthentication(usernameField.getText(),
                passwordField.getText(),
                registerCheckBox.isSelected())) app.setMainWindow();
        else if (!client.isConnected()) {
            isConnectedLabel.textProperty().bind(resourceFactory.getStringBinding("NotConnected"));
            isConnectedLabel.setTextFill(NOT_CONNECTED_COLOR);
        } else {
            isConnectedLabel.textProperty().bind(resourceFactory.getStringBinding("Connected"));
            isConnectedLabel.setTextFill(CONNECTED_COLOR);
        }
    }

    private void bindGuiLanguage() {
        usernameLabel.textProperty().bind(resourceFactory.getStringBinding("UsernameLabel"));
        passwordLabel.textProperty().bind(resourceFactory.getStringBinding("PasswordLabel"));
        registerCheckBox.textProperty().bind(resourceFactory.getStringBinding("RegisterCheckbox"));
        signInButton.textProperty().bind(resourceFactory.getStringBinding("SignInButton"));
        if (client.isConnected()) {
            isConnectedLabel.textProperty().bind(resourceFactory.getStringBinding("Connected"));
            isConnectedLabel.setTextFill(CONNECTED_COLOR);
        } else {
            isConnectedLabel.textProperty().bind(resourceFactory.getStringBinding("NotConnected"));
            isConnectedLabel.setTextFill(NOT_CONNECTED_COLOR);
        }
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void initLangs(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }
}
