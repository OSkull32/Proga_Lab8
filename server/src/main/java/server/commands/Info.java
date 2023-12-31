package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResourceFactory;

/**
 * Класс команды, которая выводит информацию о коллекции
 */
public class Info implements Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса
     *
     * @param collectionManager хранит ссылку на объект CollectionManager
     */
    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. Выводит описание коллекции HashTable
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        return collectionManager.info(user);
    }

    /**
     * @return описание команды
     * @see Command
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "InfoDescription").get();
    }
}
