package client;

import client.controllers.LoginWindowController;
import client.utility.AuthenticationHandler;
import client.utility.UserHandler;
import common.exceptions.InvalidValueException;
import common.exceptions.WrongArgumentException;
import common.utility.UserConsole;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Основной класс клиента, который создает все его экземпляры
 */
public class App extends Application {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    private static final int RECONNECTION_TIMEOUT = 5000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;

    private Stage stage;

    private static Client client;

    public static void main(String[] args) {

        String[] as = new String[]{"localhost","1821"};
        if (!initializeConnectionAddress(as)) return;

        Scanner userScanner = new Scanner(System.in);
        //AuthenticationHandler authenticationHandler = new AuthenticationHandler(userScanner);
        UserHandler userHandler = new UserHandler(userScanner);
        client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler);
        Thread t = new Thread(client::run);
        t.start();

        launch(); //запускает графический интерфейс

        //userScanner.close();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Hello!");
        try {
            loadLoginWindow();
        } catch (IOException e) {
            //todo остановить работу приложения
        }

        stage.show();
    }

    public void loadLoginWindow() throws IOException{
        FXMLLoader loginWindowLoader = new FXMLLoader(App.class.getResource("LoginWindow.fxml"));
        Scene scene = new Scene(loginWindowLoader.load());

        //init fields
        LoginWindowController loginWindowController = loginWindowLoader.getController();
        loginWindowController.setClient(client);
        loginWindowController.setApp(this);

        stage.setScene(scene);
    }

    public void loadMainWindow() throws IOException {
        FXMLLoader mainWindowLoader = new FXMLLoader(App.class.getResource("MainWindow.fxml"));
        Scene scene = new Scene(mainWindowLoader.load());
        stage.setScene(scene);
    }

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongArgumentException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new InvalidValueException();
            return true;
        } catch (WrongArgumentException ex) {
            String jarName = new File(App.class.getProtectionDomain().getCodeSource().getLocation()
                    .getPath()).getName();
            UserConsole.printCommandTextNext("Использование: 'java -jar' " + jarName + " <host> <port>'");
        } catch (NumberFormatException ex) {
            UserConsole.printCommandError("Порт должен быть представлен числом");
        } catch (InvalidValueException ex) {
            UserConsole.printCommandError("Порт не может быть отрицательным");
        }
        return false;
    }
}
