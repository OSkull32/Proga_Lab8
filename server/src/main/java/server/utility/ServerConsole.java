package server.utility;

import common.utility.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerConsole implements Console {

    private static final StringBuilder builder = new StringBuilder();
    private static final StringBuilder stringBuilder = new StringBuilder();
    private static List<String> argList = new ArrayList<>();

    @Override
    public String readLine() {
        return null;
    }

    @Override
    public void printPreamble() {
        builder.append(">");
    }

    @Override
    public void printCommandTextNext(String text) {
        builder.append(text).append('\n');
    }

    @Override
    public void printCommandText(String text) {
        builder.append(text);
    }

    @Override
    public void printCommandError(String errorText) {
        builder.append("Ошибка: ").append(errorText).append('\n');
    }

    @Override
    public void turnOnScriptMode(Scanner scanner) {

    }

    @Override
    public void turnOffScriptMode() {

    }

    public static String getAndClear() {
        String toReturn = builder.toString();
        builder.delete(0, builder.length());
        return toReturn;
    }

    public String getString() {
        return builder.toString();
    }

    public void clear() {
        builder.delete(0, builder.length());
    }

    public static String[] getArgsAndClear() {
        String[] argsAsArray = new String[argList.size()];
        argsAsArray = argList.toArray(argsAsArray);
        argList.clear();
        return argsAsArray;
    }
}
