package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;
import server.utility.ResourceFactory;

/**
 * Класс команды, которая завершает работу программы
 */
public class Exit implements Command {


    /**
     * Метод, исполняющий команду. Выводит сообщение о завершении работы программы
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        System.exit(0);
        return "";
    }

    /**
     * @return описание команды
     * @see Command
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "ExitDescription").get();
    }
}
