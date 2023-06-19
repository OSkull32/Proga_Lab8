package client.utility;

import common.data.*;
import common.exceptions.ErrorInScriptException;
import common.exceptions.InvalidValueException;
import common.exceptions.WrongArgumentException;
import common.utility.CoordinatesReaderInterface;
import common.utility.FlatReaderInterface;
import common.utility.HouseReaderInterface;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс, необходимы для чтения полей объекта Flat
 */
public class FlatReader implements FlatReaderInterface, CoordinatesReaderInterface, HouseReaderInterface {

    /**
     * Константа, хранящее шаблон (регулярное выражение), которому должны
     * соответствовать все поля {@link Flat} типа String
     */
    public static final String PATTERN_NAMES = "^[a-zA-Z0-9_\\-\\s\u0410-\u044f\u0451\u0401]+$";
    // Поле, хранящее ссылку на объект класса типа Console
    private final Scanner userScanner;
    private boolean fileMode;

    /**
     * Конструктор класса, который присваивает console значение, переданное в конструкторе в качестве параметра
     *
     * @param userScanner хранит ссылку на объект типа Scanner
     */
    public FlatReader(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    public boolean askQuestion(String question) throws ErrorInScriptException {
        String finalQuestion = question + " (yes/no):";
        String answer;
        try {
                if (!fileMode) UserConsole.printCommandTextNext(finalQuestion);
                if (!fileMode) UserConsole.printCommandText("> ");
                answer = userScanner.nextLine().trim();
                if (!fileMode) UserConsole.printCommandTextNext(answer);
                if (!answer.equals("yes") && !answer.equals("no")) throw new InvalidValueException();
                return answer.equals("yes");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("Ответ не распознан");
                if (fileMode) throw new ErrorInScriptException();
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("Ответом может быть только 'yes' или 'no'");
                if (fileMode) throw new ErrorInScriptException();
            } catch (IllegalStateException ex) {
                UserConsole.printCommandError("Непредвиденная ошибка!");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Метод, выполняющий чтение данных из консоли. Ввод полей в определенном порядке
     *
     * @return объект типа Flat
     */
    @Override
    public Flat read() {
        return new Flat(-1, readName(), readCoordinates(), LocalDateTime.now(), readArea(),
                readNumberOfRooms(), readNumberOfBathrooms(), readFurnish(), readView(), readHouse());
    }

    /**
     * Метод, который читает поле name объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля name, уже проверенное на условия допустимости
     */
    @Override
    public String readName() throws ErrorInScriptException{
        try {
            if (!fileMode) UserConsole.printCommandText("EnterName");
            String str = userScanner.nextLine().trim();
            if (str.equals("")) throw new InvalidValueException();
            else if (!str.matches(PATTERN_NAMES))
                throw new WrongArgumentException();
            else return str;
        } catch (InvalidValueException ex) {
            UserConsole.printCommandError("NameEmptyException");
        } catch (WrongArgumentException ex) {
            UserConsole.printCommandError("NameNotCorrectException");
        } catch (NoSuchElementException ex) {
            UserConsole.printCommandError("NameNotIdentifiedException");
        } catch (IllegalStateException ex) {
            UserConsole.printCommandError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает координаты x и y
     *
     * @return объект типа Coordinates
     */
    @Override
    public Coordinates readCoordinates() throws ErrorInScriptException{
        return new Coordinates(readCoordinatesX(), readCoordinatesY());
    }

    /**
     * Метод, который читает поле x объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля x, уже проверенное на условия допустимости
     */
    @Override
    public int readCoordinatesX() throws ErrorInScriptException{
        int x;
            try {
                if (!fileMode) UserConsole.printCommandText("EnterX", String.valueOf(714));
                x = Integer.parseInt(userScanner.nextLine().trim());
                if (x > 713) throw new InvalidValueException();
                else return x;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("XMustBeLessException", String.valueOf(713));
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("XMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("XNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле y объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля y, уже проверенное на условия допустимости
     */
    @Override
    public Integer readCoordinatesY() throws ErrorInScriptException{
        Integer y;
            try {
                if (!fileMode) UserConsole.printCommandText("EnterY", String.valueOf(-397));
                y = Integer.parseInt(userScanner.nextLine().trim());
                if (y <= -397) throw new InvalidValueException();
                else return y;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("YMustBeMoreException", String.valueOf(-397));
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("YMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("YNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле area объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля area, уже проверенное на условия допустимости
     */
    @Override
    public int readArea() throws ErrorInScriptException{
        int area;
            try {
                if (!fileMode) UserConsole.printCommandText("EnterArea");
                area = Integer.parseInt(userScanner.nextLine().trim());
                if (area <= 0) throw new InvalidValueException();
                else return area;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("AreaMustBeMoreZeroException");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("AreaMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("AreaNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле numberOfRooms объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfRooms, уже проверенное на условия допустимости
     */
    @Override
    public long readNumberOfRooms() throws ErrorInScriptException{
        long numberOfRooms;
            try {
                if (!fileMode) UserConsole.printCommandText("EnterNumberOfRooms");
                numberOfRooms = Long.parseLong(userScanner.nextLine().trim());
                if (numberOfRooms <= 0 || numberOfRooms > 14) throw new InvalidValueException();
                else return numberOfRooms;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("NumberOfRoomsValueException");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("NumberOfRoomsMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("NumberOfRoomsNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле numberOfBathrooms объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfBathrooms, уже проверенное на условия допустимости
     */
    @Override
    public long readNumberOfBathrooms() throws ErrorInScriptException{
        long numberOfBathrooms;
            try {
                if (!fileMode) UserConsole.printCommandText("EnterNumberOfBathrooms");
                numberOfBathrooms = Long.parseLong(userScanner.nextLine().trim());
                if (numberOfBathrooms <= 0) throw new InvalidValueException();
                else return numberOfBathrooms;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("NumberOfBathroomsValueException");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("NumberOfBathroomsMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("NumberOfBathroomsNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле furnish объекта Flat из потока, указанного в поле console.
     * Выводит список допустимых констант
     *
     * @return значение поля furnish, уже проверенное на условия допустимости
     */
    @Override
    public Furnish readFurnish() throws ErrorInScriptException{
        Furnish furnish;
            try {
                if (!fileMode) {
                    UserConsole.printCommandTextNext("FurnishList");
                    for (Furnish val : Furnish.values()) {
                        UserConsole.printCommandTextNext(val.name());
                    }
                    UserConsole.printCommandText("EnterFurnish");
                }
                furnish = Furnish.valueOf(userScanner.nextLine().toUpperCase().trim());
                return furnish;
            } catch (IllegalArgumentException ex) {
                UserConsole.printCommandError("NoSuchFurnish");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("FurnishNotIdentifiedException");
            } catch (IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле view объекта Flat из потока, указанного в поле console.
     * Выводит список допустимых констант
     *
     * @return значение поля view, уже проверенное на условия допустимости
     */
    @Override
    public View readView() throws ErrorInScriptException{
        View view;
            try {
                if (!fileMode) {
                    UserConsole.printCommandTextNext("ViewList");
                    for (View val : View.values()) {
                        UserConsole.printCommandTextNext(val.name());
                    }
                    UserConsole.printCommandText("EnterView");
                }
                String str = userScanner.nextLine().trim();
                if (str.equals("")) {
                    view = null;
                } else {
                    view = View.valueOf(str.toUpperCase().trim());
                    if (view.equals(View.NULL))
                        view = null;
                }
                return view;
            } catch (IllegalArgumentException ex) {
                UserConsole.printCommandError("NoSuchView");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("ViewNotIdentifiedException");
            } catch (IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поля House.
     *
     * @return объект типа House
     */
    @Override
    public House readHouse() throws ErrorInScriptException{
        while (true) {
            if (!fileMode)
                UserConsole.printCommandText("EnterHouse");
            String str = userScanner.nextLine().trim();
            if (str.equals(""))
                return null;
            else {
                if (!str.matches(PATTERN_NAMES))
                    UserConsole.printCommandError("HouseError");
                else return new House(str, readHouseYear(), readHouseNumberOfFloors(),
                        readHouseNumberOfFlatsOnFloor(), readHouseNumberOfLifts());
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле name объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля area, уже проверенное на условия допустимости
     */
    @Override
    public String readHouseName() throws ErrorInScriptException{
        String str;

        try {
            if (!fileMode) UserConsole.printCommandText("EnterHouseName");
            str = userScanner.nextLine().trim();
            if (str.equals("")) throw new InvalidValueException();
            else if (!str.matches(PATTERN_NAMES))
                throw new WrongArgumentException();
            else return str;
        } catch (InvalidValueException ex) {
            UserConsole.printCommandError("NameEmptyException");
        } catch (WrongArgumentException ex) {
            UserConsole.printCommandError("NameNotCorrectException");
        } catch (NoSuchElementException ex) {
            UserConsole.printCommandError("NameNotIdentifiedException");
        } catch (IllegalStateException ex) {
            UserConsole.printCommandError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
    }

    /**
     * Метод, который читает поле year объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля year, уже проверенное на условия допустимости
     */
    @Override
    public int readHouseYear() throws ErrorInScriptException{
        int houseYear;
            try {
                if (!fileMode) UserConsole.printCommandText("EnterHouseYear");
                houseYear = Integer.parseInt(userScanner.nextLine().trim());
                if (houseYear <= 0) throw new InvalidValueException();
                else return houseYear;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("HouseYearMustBeMoreZeroException");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("HouseYearMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("HouseYearNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле numberOfFloors объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfFloors, уже проверенное на условия допустимости
     */
    @Override
    public Long readHouseNumberOfFloors() throws ErrorInScriptException{
        Long numberOfFloors;
            try {
                if (!fileMode) UserConsole.printCommandText("EnterHouseNumberOfFloors");
                String str = userScanner.nextLine().trim();
                if (str.equals("")) numberOfFloors = null;
                else {
                    numberOfFloors = Long.parseLong(str);
                    if (numberOfFloors <= 0 || numberOfFloors > 39) throw new InvalidValueException();
                }
                return numberOfFloors;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("HouseNumberOfFloorsValueException");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("HouseNumberOfFloorsMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("HouseNumberOfFloorsNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле numberOfFlatsOnFloor объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfFlatsOnFloor, уже проверенное на условия допустимости
     */
    @Override
    public long readHouseNumberOfFlatsOnFloor()  throws ErrorInScriptException{
        long numberOfFlatsOnFloor;
            try {
                if (!fileMode)
                    UserConsole.printCommandText("EnterHouseNumberOfFlatsOnFloor");
                numberOfFlatsOnFloor = Long.parseLong(userScanner.nextLine().trim());
                if (numberOfFlatsOnFloor <= 0) throw new InvalidValueException();
                else return numberOfFlatsOnFloor;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("HouseNumberOfFlatsOnFloorMustBeMoreZeroException");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("HouseNumberOfLiftsMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("HouseNumberOfLiftsNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

    /**
     * Метод, который читает поле numberOfLifts объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfLifts, уже проверенное на условия допустимости
     */
    @Override
    public Long readHouseNumberOfLifts() throws ErrorInScriptException{
        Long numberOfLifts;
            try {
                if (!fileMode) UserConsole.printCommandText("EnterHouseNumberOfLifts");
                String str = userScanner.nextLine().trim();
                if (str.equals("")) numberOfLifts = null;
                else {
                    numberOfLifts = Long.parseLong(str);
                    if (numberOfLifts <= 0) throw new InvalidValueException();
                }
                return numberOfLifts;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("HouseNumberOfLiftsMustBeMoreZeroException");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("HouseNumberOfFlatsOnFloorMustBeNumberException");
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("HouseNumberOfFlatsOnFloorNotIdentifiedException");
            } catch (NullPointerException | IllegalStateException ex) {
                UserConsole.printCommandError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        throw new ErrorInScriptException();
    }

}
