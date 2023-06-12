package server.commands;

import common.data.Flat;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;

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
        if (hashtable.size() == 0) {
            builder.append("Коллекция пуста\n");
        } else {
            AtomicInteger number = new AtomicInteger(1);
            hashtable.forEach((key, flat) -> {
                try {
                    builder.append("\nЭлемент №").append(number.getAndIncrement()).append("\n")
                            .append(flat.toString()).append("\n").append("User:").append(databaseCollectionManager.getUsernameByFlatId(flat.getId())).append("\n");
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
    public String getDescription() {
        return "Показывает содержимое всех элементов коллекции";
    }
}
