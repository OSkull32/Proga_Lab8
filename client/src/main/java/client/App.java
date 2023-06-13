package client;

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

    public static void main(String[] args) {
        launch();
        String[] as = new String[]{"localhost","1821"};
        if (!initializeConnectionAddress(as)) return;
        Scanner userScanner = new Scanner(System.in);
        AuthenticationHandler authenticationHandler = new AuthenticationHandler(userScanner);
        UserHandler userHandler = new UserHandler(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler, authenticationHandler);
        client.run();
        userScanner.close();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LoginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
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
