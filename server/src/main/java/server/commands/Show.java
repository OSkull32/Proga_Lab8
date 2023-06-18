package server.commands;

import common.data.Flat;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResourceFactory;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс команды, которая показывает содержимое коллекции
 */
public class Show implements Command {

    private final DatabaseCollectionManager databaseCollectionManager;

    private final CollectionManager collectionManager;

    /**
     * Конструктор класса
     *
     * @param collectionManager хранит ссылку на объект CollectionManager
     */
    public Show(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, исполняющий команду. Выводит содержимое коллекции
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        Hashtable<Integer, Flat> hashtable = collectionManager.getCollection();
        var builder = new StringBuilder();
        var lang = user.getLanguage();
        if (hashtable.size() == 0) {
            builder.append(ResourceFactory.getStringBinding(lang, "ShowCollectionIsEmpty").get()).append("\n");
        } else {
            AtomicInteger number = new AtomicInteger(1);
            var wordElement = ResourceFactory.getStringBinding(lang, "ShowElement").get();
            var wordUser = ResourceFactory.getStringBinding(lang, "ShowUser").get();
            hashtable.forEach((key, flat) -> {
                try {
                    builder.append("\n").append(wordElement).append(" №").append(number.getAndIncrement()).append("\n")
                            .append(flat.toString()).append("\n").append(wordUser).append(": ")
                            .append(databaseCollectionManager.getUsernameByFlatId(flat.getId())).append("\n");
                } catch (SQLException e) {
                    builder.append("Произошла ошибка при обращении к БД").append("\n");
                }
            });
        }
        return builder.toString();
    }

    /**
     * @return описание команды
     * @see Command
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "ShowDescription").get();
    }
}
