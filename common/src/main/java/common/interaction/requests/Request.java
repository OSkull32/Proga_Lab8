package common.interaction.requests;

import common.interaction.User;

import java.io.Serializable;

/**
 * Класс для получения значения запроса.
 */
public class Request implements Serializable {
    private String commandName;
    private String commandStringArgument;
    private Serializable commandObjectArgument;
    private User user;
    private String language;

    public Request(String commandName, String commandStringArgument, Serializable commandObjectArgument, User user, String language) {
        this.commandName = commandName;
        this.commandStringArgument = commandStringArgument;
        this.commandObjectArgument = commandObjectArgument;
        this.user = user;
        this.language = language;
    }

    public Request(String commandName, String commandStringArgument, Serializable commandObjectArgument, User user) {
        this(commandName, commandStringArgument, commandObjectArgument, user, "");
    }

    public Request(String commandName, String commandStringArgument, User user) {
        this(commandName, commandStringArgument, null, user, "");
    }

    public Request(User user) {
        this("","", user);
    }

    /**
     * @return Имя команды
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Аргумент командной строки.
     */
    public String getCommandStringArgument() {
        return commandStringArgument;
    }

    /**
     * @return Аргумент объекта команды.
     */
    public Object getCommandObjectArgument() {
        return commandObjectArgument;
    }

    public User getUser() {
        return user;
    }

    /**
     * @return Пустой ли запрос
     */
    public boolean isEmpty() {
        return commandName.isEmpty() && commandStringArgument.isEmpty() && commandObjectArgument == null && user == null;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "Request[" + commandName + ", " + commandStringArgument + ", " + commandObjectArgument + ", " + user + "]";
    }
}