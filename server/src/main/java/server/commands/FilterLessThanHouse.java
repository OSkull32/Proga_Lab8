package server.commands;

import common.data.House;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.ResourceFactory;
import server.utility.SortByCoordinates;

/**
 * Класс команды "filter_less_than_house".
 *
 * @author Kliodt Vadim
 * @version 1.0
 */

public class FilterLessThanHouse implements Command {

    private final CollectionManager collectionManager;

    /**
     * Конструирует объект, привязывая его к конкретному объекту {@link CollectionManager}.
     *
     * @param collectionManager указывает на объект {@link CollectionManager}.
     */
    public FilterLessThanHouse(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Метод запускает исполнение команды "filter_less_then_house".
     *
     * @param args Строка, содержащая переданные команде аргументы.
     * @throws WrongArgumentException при неправильном аргументе.
     */
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        var lang = user.getLanguage();
        try {
            int year;
            Long numberOfFloors;
            long numberOfFlatsOnFloor;
            Long numberOfLifts;

            if (objectArgument == null) {
                builder.append(ResourceFactory.getStringBinding(lang, "FilterLessThanHouseWrongHouse").get()).append(" == NULL\n");
                return builder.toString();
            }
            if (objectArgument instanceof House house) { //pattern variable
                year = house.getYear();
                numberOfFloors = house.getNumberOfFloors();
                numberOfFlatsOnFloor = house.getNumberOfFlatsOnFloor();
                numberOfLifts = house.getNumberOfLifts();
            } else {
                throw new WrongArgumentException(ResourceFactory.getStringBinding(lang, "FilterLessThanHouseTypeError").get());
            }
            var wordFlat = ResourceFactory.getStringBinding(lang, "FilterLessThanHouseFlat").get() + ": ";
            var wordHouse = ResourceFactory.getStringBinding(lang, "FilterLessThanHouseHouse").get() + ": ";
            collectionManager.getCollection().values().stream()
                    .filter(flat -> flat.getHouse() != null
                            && flat.getHouse().getYear() < year
                            && flat.getHouse().getNumberOfFloors() != null && numberOfFloors != null && flat.getHouse().getNumberOfFloors() < numberOfFloors
                            && flat.getHouse().getNumberOfFlatsOnFloor() < numberOfFlatsOnFloor
                            && flat.getHouse().getNumberOfLifts() != null && numberOfLifts != null && flat.getHouse().getNumberOfLifts() < numberOfLifts
                    )
                    .sorted(new SortByCoordinates())
                    .forEach(flat -> builder.append(wordFlat).append(flat.getName()).append(" ").append(wordHouse).append(flat.getHouse().getName()).append(";\n"));

        } catch (NumberFormatException e) {
            throw new WrongArgumentException(ResourceFactory.getStringBinding(lang, "FilterLessThanHouseWrongArgument").get());
        } catch (Exception ex) { //на случай непредвиденного null pointer (хотя по идее не должно возникнуть)
            builder.append(ResourceFactory.getStringBinding(lang, "Error").get()).append(ResourceFactory.getStringBinding(lang, "FilterLessThanHouseWrongArgument2").get());
        }
        if (builder.toString().equals(""))
            builder.append(ResourceFactory.getStringBinding(lang, "Error").get()).append(ResourceFactory.getStringBinding(lang, "FilterLessThanHouseNoSuchHouse").get());
        return builder.toString();
    }

    /**
     * Получить описание команды.
     *
     * @return описание команды.
     */
    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "FilterLessThanHouseDescription").get();
    }
}
