package client.utility;

import client.gui.ResourceFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.text.MessageFormat;
import java.util.MissingResourceException;

/**
 * Класс для вывода чего-либо пользователю.
 */
public class OutputerUI {
    private static final String INFO_TITLE = "Collection Keeper";
    private static final String ERROR_TITLE = "Collection Keeper";

    private static ResourceFactory resourceFactory;

    /**
     * Формирует сообщение
     *
     * @param title Название сообщения.
     * @param toOut Сообщение.
     * @param args Аргументы.
     * @param msgType Тип сообщения.
     */
    private static void msg(String title, String toOut, String[] args, AlertType msgType) {
        Alert alert = new Alert(msgType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(tryResource(toOut, args));
        alert.showAndWait();
    }

    private static String tryResource(String str, String[] args) {
        try {
            if (haveResourceFactory()) throw new NullPointerException();
            if (args == null) return resourceFactory.getResources().getString(str);
            MessageFormat messageFormat = new MessageFormat(resourceFactory.getResources().getString(str));
            return messageFormat.format(args);
        } catch (MissingResourceException | NullPointerException exception) {
            return str;
        }
    }

    public static void info(String toOut, String[] args) {
        msg(INFO_TITLE, toOut, args, AlertType.INFORMATION);
    }

    public static void info(String toOut) {
        info(toOut, null);
    }

    public static void error(String toOut, String[] args) {
        msg(ERROR_TITLE, toOut, args, AlertType.ERROR);
    }


    public static void error(String toOut) {
        error(toOut, null);
    }

    /**
     * Выводит ошибку, если объект начинается с 'error:', или информацию в другом случае.
     *
     * @param toOut Сообщение для печати.
     * @param args Аргументы
     */
    public static void tryError(String toOut, String[] args) {
        if (toOut.startsWith("error: "))
            msg(ERROR_TITLE, toOut.substring(7), args, AlertType.ERROR);
        else msg(INFO_TITLE, toOut, args, AlertType.INFORMATION);
    }

    public static void tryErrorScript(String toOut, String[] args) {
        if (toOut.startsWith("error: ") || toOut.startsWith("ошибка: ") || toOut.startsWith("greška: ") || toOut.startsWith("памылка: "))
            msg(ERROR_TITLE, toOut.substring(7), args, AlertType.ERROR);
    }

    public static void tryScript() {
        msg(INFO_TITLE, "ScriptEnd", new String[]{""}, AlertType.INFORMATION);
    }

    /**
     * Выводит ошибку, если объект начинается с 'error:', или информацию в другом случае.
     *
     * @param toOut Сообщение для печати.
     */
    public static void tryError(String toOut) {
        tryError(toOut, null);
    }

    public static void setResourceFactory(ResourceFactory resourceFactory) {
        OutputerUI.resourceFactory = resourceFactory;
    }

    /**
     * Проверка на фабрику ресурсов.
     *
     * @return False, если есть, и true, если нет
     */
    public static boolean haveResourceFactory() {
        return resourceFactory == null;
    }
}
