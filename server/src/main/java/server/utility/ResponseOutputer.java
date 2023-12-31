package server.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс для генерации ответов клиенту
 */
public class ResponseOutputer {
    private static StringBuilder stringBuilder = new StringBuilder();
    private static List<String> argList = new ArrayList<>();

    /**
     * Добавляет объект в конец строки
     *
     * @param object объект
     */
    public static void append(Object object) {
        stringBuilder.append(object);
    }

    /**
     * Добавляет переход на следующую строку
     */
    public static void appendln() {
        stringBuilder.append("\n");
    }

    /**
     * Добавляет объект в конец строки и переход на следующую строку
     *
     * @param object объект
     */
    public static void appendln(String object) {
        stringBuilder.append(object).append("\n");
    }

    public static void appendargs(String... args) {
        argList.addAll(Arrays.asList(args));
    }

    /**
     * Добавляет описание ошибки и переход строки
     *
     * @param object описание ошибки
     */
    public static void appendError(Object object) {
        stringBuilder.append("error: ").append(object).append("\n");
    }

    /**
     * Добавляет таблицу с двумя элементами в конец строки
     *
     * @param element1 1 элемент
     * @param element2 2 элемент
     */
    public static void appendTable(Object element1, Object element2) {
        stringBuilder.append(String.format("%-37s%-1s%n", element1, element2));
    }

    /**
     * делает string
     *
     * @return перевод в string
     */
    public static String getString() {
        return stringBuilder.toString();
    }

    /**
     * Делает string и очищает буфер
     *
     * @return перевод в string.
     */
    public static String getAndClear() {
        String toReturn = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return toReturn;
    }

    /**
     * Очистка буфера
     */
    public static void clear() {
        stringBuilder.delete(0, stringBuilder.length());
    }

    public static String[] getArgsAndClear() {
        String[] argsAsArray = new String[argList.size()];
        argsAsArray = argList.toArray(argsAsArray);
        argList.clear();
        return argsAsArray;
    }
}
