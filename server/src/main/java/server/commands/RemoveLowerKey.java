package server.commands;

import common.data.Flat;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResourceFactory;

import java.util.ArrayList;

/**
 * Класс команды, удаляющий элементы, у которых id меньше заданного ключа
 */
public class RemoveLowerKey implements Command {
    private final DatabaseCollectionManager databaseCollectionManager;
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveLowerKey(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, удаляющий все элементы коллекции, значения id которых меньше заданного ключа
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        var lang = user.getLanguage();
        int size = 0;
        try {
            int key = Integer.parseInt(args);
            ArrayList<Integer> keys = new ArrayList<>();
            long  count = collectionManager.getCollection().entrySet().stream()
                    .filter(entry -> entry.getKey() < key)
                    .peek(entry -> keys.add(entry.getKey())).count();
            for (Integer k : keys) {
                Flat flat = collectionManager.getCollection().get(k);
                if (databaseCollectionManager.checkFlatUserId(flat.getId(), user) && flat.getOwner().equals(user)) {
                    databaseCollectionManager.deleteFlatById(flat.getId());
                    collectionManager.removeKey(k);
                    size += 1;
                }
            }
            if (size == 0) builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeyNoLowerElements").get()).append("\n");
            else builder.append(count).append(" ").append(ResourceFactory.getStringBinding(lang, "RemoveKeyElementsDeleted").get()).append("\n");
        } catch (IndexOutOfBoundsException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "Error").get()).append(ResourceFactory.getStringBinding(lang, "RemoveKeyWrongArgument").get()).append("\n");
        } catch (NumberFormatException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "Error").get()).append(ResourceFactory.getStringBinding(lang, "RemoveKeyWrongArgumentFormat").get()).append(ex.getMessage()).append("\n");
        } catch (DatabaseHandlingException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "Error").get()).append(ResourceFactory.getStringBinding(lang, "RemoveKeyErrorDatabase").get()).append("\n");
        }
        return builder.toString();
    }

    /**
     * @return описание команды.
     * @see Command
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "RemoveLowerKeyDescription").get();
    }
}
