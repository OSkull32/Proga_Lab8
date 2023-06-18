package server.commands;

import common.data.Flat;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.PermissionDeniedException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResourceFactory;

/**
 * Класс команды, которая удаляет элемент
 */
public class RemoveKey implements Command {
    private final DatabaseCollectionManager databaseCollectionManager;
    private final CollectionManager collectionManager;

    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на объект CollectionManager.
     */
    public RemoveKey(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Метод, удаляющий элемент коллекции, по значению ключа
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        var lang = user.getLanguage();
        try {
            int key = Integer.parseInt(args);
            if (collectionManager.containsKey(key)) {
                Flat flatToRemove = collectionManager.getCollection().get(key);
                if (!flatToRemove.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkFlatUserId(flatToRemove.getId(), user)) throw new ManualDatabaseEditException();
                databaseCollectionManager.deleteFlatById(key);
                collectionManager.removeKey(Integer.parseInt(args));
                builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeySuccess").get()).append("\n");
            } else builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeyNoSuchItem").get()).append("\n");
        } catch (IndexOutOfBoundsException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeyWrongArgument").get()).append("\n");
        } catch (NumberFormatException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeyWrongArgumentFormat").get()).append(ex.getMessage()).append("\n");
        } catch (PermissionDeniedException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeyNotEnoughRights").get()).append("\n");
            builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeyCantChangeNotYourObjects").get()).append("\n");
        } catch (ManualDatabaseEditException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeyErrorDatabaseChanged").get()).append("\n");
        } catch (DatabaseHandlingException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "RemoveKeyErrorDatabase").get()).append("\n");
        }
        return builder.toString();
    }

    /**
     * @return Возвращает описание команды
     * @see Command
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "RemoveKeyDescription").get();
    }
}
