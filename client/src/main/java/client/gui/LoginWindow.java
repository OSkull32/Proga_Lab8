package client.gui;

import client.App;
import client.Client;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ResourceBundle;


public class LoginWindow {
    private final Color CONNECTED_COLOR = Color.GREEN;
    private final Color NOT_CONNECTED_COLOR = Color.RED;

    private App app;
    private Client client;
    private ResourceFactory resourceFactory;

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
    @FXML
    private ComboBox<String> languageComboBox;

    public void initialize() {
        languageComboBox.setItems(FXCollections.observableArrayList(ResourceFactory.LOCALE_MAP.keySet()));
    }

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

    @FXML
    void updateLanguages(ActionEvent event) {
        resourceFactory.setResources(ResourceBundle.getBundle
                (App.BUNDLE, ResourceFactory.LOCALE_MAP.get(languageComboBox.getValue())));
        bindGuiLanguage();
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
