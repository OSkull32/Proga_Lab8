package client;

import client.gui.AskWindow;
import client.gui.LoginWindow;
import client.gui.MainWindow;
import client.gui.ResourceFactory;
import client.utility.OutputerUI;
import common.exceptions.InvalidValueException;
import common.exceptions.WrongArgumentException;
import client.utility.UserConsole;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Основной класс клиента, который создает все его экземпляры
 */
public class App extends Application {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";
    public static final String BUNDLE = "bundles.gui";

    private static final int RECONNECTION_TIMEOUT = 5000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;
    private static final String APP_TITLE = "Collection Keeper";

    private static String host;
    private static Client client;
    private static int port;
    private Stage primaryStage;
    private static ResourceFactory resourceFactory;
    private static Scanner userScanner;

    public static void main(String[] args) {
        resourceFactory = new ResourceFactory();
        resourceFactory.setResources(ResourceBundle.getBundle(BUNDLE));
        OutputerUI.setResourceFactory(resourceFactory);
        UserConsole.setResourceFactory(resourceFactory);

        if (initialize(args)) launch(args);
        else System.exit(0);
    }

    private static boolean initialize(String[] args) {
        try {
            if (args.length != 2) throw new WrongArgumentException();
            host = args[0];
            port = Integer.parseInt(args[1]);
            if (port < 0) throw new InvalidValueException();
            return true;
        } catch (WrongArgumentException exception) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            UserConsole.printCommandError("Using 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            UserConsole.printCommandError("PortMustBeNumber");
        } catch (InvalidValueException exception) {
            UserConsole.printCommandError("PortMustBeNotNegative");
        }
        return false;
    }

    @Override
    public void start(Stage stage) {
        try {
            this.primaryStage = stage;

            FXMLLoader loginWindowLoader = new FXMLLoader();
            loginWindowLoader.setLocation(getClass().getResource("/view/LoginWindow.fxml"));
            Parent loginWindowRootNode = loginWindowLoader.load();
            Scene loginWindowScene = new Scene(loginWindowRootNode);
            LoginWindow loginWindow = loginWindowLoader.getController();
            loginWindow.setApp(this);
            loginWindow.setClient(client);
            loginWindow.initLangs(resourceFactory);

            primaryStage.setTitle(APP_TITLE);

            primaryStage.setScene(loginWindowScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception exception) {
            // TODO: Обработать ошибки
            System.out.println(exception);
            exception.printStackTrace();
        }
    }

    @Override
    public void init() {
        userScanner = new Scanner(System.in);
        client = new Client(host, port);

        client.run();
    }

    @Override
    public void stop() {
        client.stop();
        userScanner.close();
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

    public void setMainWindow() {
        try {
            FXMLLoader mainWindowLoader = new FXMLLoader();
            mainWindowLoader.setLocation(getClass().getResource("/view/MainWindow.fxml"));
            Parent mainWindowRootNode = mainWindowLoader.load();
            Scene mainWindowScene = new Scene(mainWindowRootNode);
            MainWindow mainWindow = mainWindowLoader.getController();
            mainWindow.initLangs(resourceFactory);

            FXMLLoader askWindowLoader = new FXMLLoader();
            askWindowLoader.setLocation(getClass().getResource("/view/AskWindow.fxml"));
            Parent askWindowRootNode = askWindowLoader.load();
            Scene askWindowScene = new Scene(askWindowRootNode);
            Stage askStage = new Stage();
            askStage.setTitle(APP_TITLE);
            askStage.setScene(askWindowScene);
            askStage.setResizable(false);
            askStage.initModality(Modality.WINDOW_MODAL);
            askStage.initOwner(primaryStage);
            AskWindow askWindow = askWindowLoader.getController();
            askWindow.setAskStage(askStage);
            askWindow.initLangs(resourceFactory);

            mainWindow.setClient(client);
            mainWindow.setUsername(client.getUsername());
            mainWindow.setAskStage(askStage);
            mainWindow.setPrimaryStage(primaryStage);
            mainWindow.setAskWindow(askWindow);
            mainWindow.refreshButtonOnAction();

            primaryStage.setScene(mainWindowScene);
            primaryStage.setMinWidth(mainWindowScene.getWidth());
            primaryStage.setMinHeight(mainWindowScene.getHeight());
            primaryStage.setResizable(true);
        } catch (Exception exception) {
            // TODO: Обработать ошибки
            System.out.println(exception);
            exception.printStackTrace();
        }
    }
}
