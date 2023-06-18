package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.ResourceFactory;

/**
 * Класс команды "history".
 *
 * @author Kliodt Vadim
 * @version 1.0
 */
public class History implements Command {
    private final CommandManager commandManager;

    /**
     * Конструирует объект, привязывая его к конкретному объекту {@link CommandManager}.
     *
     * @param commandManager указывает на объект {@link CommandManager}, в котором
     *                       будет вызываться соответствующий метод {@link CommandManager#getHistoryList()}.
     */
    public History(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду "history".
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        return "";
    }

    /**
     * Метод, описывающий работу команды
     *
     * @return Возвращает описание команды
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "HistoryDescription").get();
    }
}
