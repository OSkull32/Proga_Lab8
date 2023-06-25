package server.commands;

import common.data.Flat;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongArgumentException;
import common.interaction.FlatValue;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResourceFactory;

import java.util.ArrayList;
import java.util.Map;

/**
 * Команда, очищающая коллекцию
 */
public class Clear implements Command {
    private final DatabaseCollectionManager databaseCollectionManager;

    // Поле, хранящее ссылку на объект класса collectionManager
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса
     *
     * @param collectionManager хранит ссылку на объект CollectionManager
     */
    public Clear(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Метод, исполняющий команду. Выводит сообщение о том, что коллекция очищена
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        var lang = user.getLanguage();
        int size = 0;
        try {
            ArrayList<Integer> keys = new ArrayList<>();
            for (Map.Entry<Integer, Flat> entry : collectionManager.getCollection().entrySet()) {
                keys.add(entry.getKey());
            }
            for (Integer k : keys) {
                Flat flat = collectionManager.getCollection().get(k);
                if (databaseCollectionManager.checkFlatUserId(flat.getId(), user) && flat.getOwner().equals(user)) {
                    databaseCollectionManager.deleteFlatById(flat.getId());
                    collectionManager.removeKey(k);
                    size += 1;
                }
            }
            if (size == 0) builder.append(ResourceFactory.getStringBinding(lang, "ClearNoYoursElements").get()).append("\n");
            else builder.append(ResourceFactory.getStringBinding(lang, "ClearDone").get()).append("\n");
        } catch (DatabaseHandlingException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "Error").get()).append(ResourceFactory.getStringBinding(lang, "ClearError").get()).append("\n");
        }
        return builder.toString();
    }

    /**
     * @return Описание команды
     * @see Command
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "ClearDescription").get();
    }
}
