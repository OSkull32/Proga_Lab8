package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.ResourceFactory;

/**
 * Класс команды "help".
 *
 * @author Kliodt Vadim
 * @version 1.1
 */
public class Help implements Command {
    private final CommandManager commandManager;

    /**
     * Конструирует объект, привязывая его к конкретному объекту {@link CommandManager}.
     *
     * @param commandManager указывает на объект {@link CommandManager}, в котором
     *                       будет вызываться соответствующий метод {@link CommandManager#getCommandsInfo(User)}.
     */
    public Help(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду "help".
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        return commandManager.getCommandsInfo(user);
    }

    /**
     * Метод, описывающий работу команды
     *
     * @return Возвращает описание команды
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "HelpDescription").get();
    }
}
