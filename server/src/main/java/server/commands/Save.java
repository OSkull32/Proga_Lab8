package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.Console;
import common.utility.FileManager;
import server.utility.CollectionManager;
import server.utility.JsonParser;
import server.utility.ResourceFactory;

/**
 * Класс, который сохраняет коллекцию в файл.
 *
 * @author Kliodt Vadim
 * @version 1.0
 */
public class Save implements Command {
    private final CollectionManager collectionManager;
    private final Console console;
    private final FileManager fileManager;

    /**
     * Конструктор класса
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public Save(CollectionManager collectionManager, Console console, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.console = console;
        this.fileManager = fileManager;
    }

    /**
     * Метод, сохраняющий коллекцию в указанном файле
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        fileManager.writeToFile(JsonParser.encode(collectionManager.getCollection()));
        console.printCommandTextNext("Коллекция была сохранена.");
        return "";
    }

    /**
     * Получить описание команды.
     *
     * @return описание команды.
     * @see Command
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "SaveDescription").get();
    }

}
