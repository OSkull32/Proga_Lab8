package client.utility;

import client.App;
import common.data.*;
import common.exceptions.CommandUsageException;
import common.exceptions.ErrorInScriptException;
import common.exceptions.RecursiveException;
import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.ResponseCode;
import common.utility.FlatReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class ScriptHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();
    private final String language;

    public ScriptHandler(File scriptFile, String language) {
        this.language = language;
        try {
            userScanner = new Scanner(scriptFile);
            scannerStack.add(userScanner);
            scriptStack.add(scriptFile);
        } catch (Exception exception) { /* ? */ }
    }

    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (serverResponseCode == ResponseCode.ERROR || serverResponseCode == ResponseCode.SERVER_EXIT)
                        throw new ErrorInScriptException();
                    while (!scannerStack.isEmpty() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        if (!scannerStack.isEmpty()) scriptStack.pop();
                        else return null;
                    }
                    userInput = userScanner.nextLine();
                    if (!userInput.isEmpty()) {
                        UserConsole.printCommandTextNext(App.PS1);
                        UserConsole.printCommandTextNext(userInput);
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    UserConsole.printCommandTextNext("CommandErrorException");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        UserConsole.printCommandError("RewriteAttemptsException");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (userCommand[0].isEmpty());
            try {
                if (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR)
                    throw new ErrorInScriptException();
                switch (processingCode) {
                    case OBJECT -> {
                        Flat flatAddValue = generateFlatAdd();
                        return new Request(userCommand[0], userCommand[1], flatAddValue, user, language);
                    }
                    case UPDATE_OBJECT -> {
                        Flat flatUpdateValue = generateFlatUpdate();
                        return new Request(userCommand[0], userCommand[1], flatUpdateValue, user, language);
                    }
                    case UPDATE_OBJECT_HOUSE -> {
                        House houseFilterValue = generateHouseFilter();
                        return new Request(userCommand[0], userCommand[1], houseFilterValue, user, language);
                    }
                    case SCRIPT -> {
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new RecursiveException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        UserConsole.printCommandTextNext("Выполняю скрипт '" + scriptFile.getName() + "'...");
                    }
                }
            } catch (FileNotFoundException exception) {
                UserConsole.printCommandError("ScriptFileNotFoundException");
                throw new ErrorInScriptException();
            } catch (RecursiveException exception) {
                UserConsole.printCommandError("ScriptRecursionException");
                throw new ErrorInScriptException();
            }
        } catch (ErrorInScriptException exception) {
            OutputerUI.error("IncorrectInputInScriptException");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return null;
        }
        return new Request(userCommand[0], userCommand[1], null, user, language);
    }

    /**
     * Обрабатывает введенную команду.
     *
     * @return Статус кода.
     */
    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            switch (command) {
                case "" -> {
                    return ProcessingCode.ERROR;
                }
                case "info", "show", "clear", "exit", "help", "server_exit" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                }
                case "insert" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                }
                case "update" -> {
                    if (commandArgument.isEmpty() || Integer.parseInt(commandArgument) <= 0) throw new CommandUsageException("<Key'>0'> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                }
                case "remove_key", "remove_greater_key", "remove_lower_key" -> {
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<Key>");
                }
                case "execute_script" -> {
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                }
                case "remove_all_by_view" -> {
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<View>");
                }
                case "filter_less_than_house" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    return ProcessingCode.UPDATE_OBJECT_HOUSE;
                }
                case "print_field_ascending_house" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("<House>");
                }
                case "history" -> {
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("<History>");
                }
                default -> {
                    UserConsole.printCommandTextNext("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessingCode.ERROR;
                }
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            UserConsole.printCommandTextNext("Использование: '" + command + "'");
            return ProcessingCode.ERROR;
        } catch (NumberFormatException ex) {
            UserConsole.printCommandError("Формат аргумента не соответствует целочисленному");
        }
        return ProcessingCode.OK;
    }

    /**
     * Проверяет находиться ли обработчик в файловом режиме
     *
     * @return находиться ли обработчик в файловом режиме
     */
    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }

    /**
     * Генерирует Flat на обновление
     *
     * @return flat на обновление
     * @throws ErrorInScriptException когда что-то не так в скрипте
     */
    private Flat generateFlatUpdate() throws ErrorInScriptException {
        FlatReader flatReader = new FlatReader(userScanner);
        if (fileMode()) flatReader.setFileMode();
        String name = flatReader.askQuestion("Хотите поменять имя квартиры?") ?
                flatReader.readName() : null;
        Coordinates coordinates = flatReader.askQuestion("Хотите изменить координаты квартиры?") ?
                flatReader.readCoordinates() : null;
        int area = (flatReader.askQuestion("Хотите изменить площадь квартиры?") ?
                flatReader.readArea() : -1);
        long numberOfRooms = (flatReader.askQuestion("Хотите изменить количество комнат?") ?
                flatReader.readNumberOfRooms() : -1);
        long numberOfBathrooms = (flatReader.askQuestion("Хотите изменить количество ванных комнат?") ?
                flatReader.readNumberOfBathrooms() : -1);
        Furnish furnish = (flatReader.askQuestion("Хотите изменить мебель в квартире?") ?
                flatReader.readFurnish() : null);
        View view = (flatReader.askQuestion("Хотите изменить вид из квартиры?") ?
                flatReader.readView() : null);
        House house = (flatReader.askQuestion("Хотите изменить house в квартире?") ?
                flatReader.readHouse() : null);
        return new Flat(
                name,
                coordinates,
                area,
                numberOfRooms,
                numberOfBathrooms,
                furnish,
                view,
                house
        );
    }

    private Flat generateFlatAdd() throws ErrorInScriptException {
        FlatReader flatReader = new FlatReader(userScanner);
        if (fileMode()) flatReader.setFileMode();
        return new Flat(
                flatReader.readName(),
                flatReader.readCoordinates(),
                flatReader.readArea(),
                flatReader.readNumberOfRooms(),
                flatReader.readNumberOfBathrooms(),
                flatReader.readFurnish(),
                flatReader.readView(),
                flatReader.readHouse()
        );
    }

    private House generateHouseFilter() {
        FlatReader flatReader = new FlatReader(userScanner);
        if (fileMode()) flatReader.setFileMode();
        return new House(null,
                flatReader.readHouseYear(),
                flatReader.readHouseNumberOfFloors(),
                flatReader.readHouseNumberOfFlatsOnFloor(),
                flatReader.readHouseNumberOfLifts()
        );
    }
}
