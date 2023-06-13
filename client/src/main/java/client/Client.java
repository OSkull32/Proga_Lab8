package client;

import client.gui.MainWindow;
import client.utility.AuthenticationHandler;
import client.utility.OutputerUI;
import client.utility.ScriptHandler;
import client.utility.UserHandler;
import common.data.Flat;
import common.exceptions.ConnectionErrorException;
import common.exceptions.InvalidValueException;
import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import client.utility.UserConsole;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;

/**
 * Класс запускающий клиент
 */
public class Client implements Runnable{
    private final String host;
    private final int port;
    private int reconnectionTimeout;
    private int maxReconnectionAttempts;
    private UserHandler userHandler;
    private int reconnectionAttempts;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private AuthenticationHandler authenticationHandler;
    private User user;
    private boolean isConnected;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    public void run() {
        try {
            connectToServer();
        } catch (InvalidValueException exception) {
            UserConsole.printCommandError("ClientException");
            System.exit(0);
        } catch (ConnectionErrorException exception) { /* ? */ }
    }

    public void stop() {
        try {
            processRequestToServer(MainWindow.EXIT_COMMAND_NAME, "", null);
            socketChannel.close();
            UserConsole.printCommandTextNext("EndWorkOfClient");
        } catch (IOException | NullPointerException exception) {
            UserConsole.printCommandError("EndWorkOfClientException");
            if (socketChannel == null) UserConsole.printCommandError("EndRunningWorkOfClientException");
        }
    }

    /**
     * Начинает работу с клиентом
     */
    /*public void run() {
        try {
            while (true) {
                try {
                    connectToServer();
                    processAuthentication();
                    processRequestToServer();
                    break;
                } catch (ConnectionErrorException ex) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        UserConsole.printCommandError("Превышено количество попыток подключения");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutEx) {
                        UserConsole.printCommandError("Время ожидания подключения '" + reconnectionTimeout +
                                "' находится за пределом допустимого значения");
                        UserConsole.printCommandTextNext("Производиться повторное подключение");
                    } catch (Exception timeoutEx) {
                        UserConsole.printCommandError("Произошла ошибка при попытке ожидания подключения");
                        UserConsole.printCommandTextNext("Производиться повторное подключение");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            UserConsole.printCommandTextNext("Работа клиента успешно завершена");
        } catch (InvalidValueException ex) {
            UserConsole.printCommandError("Клиент не может быть запущен");
        } catch (IOException ex) {
            UserConsole.printCommandError("Произошла ошибка при попытке завершить соединение с сервером");
        }
    }*/

    /**
     * Подключение к серверу
     *
     * @throws ConnectionErrorException ошибка при соединении
     * @throws InvalidValueException    невалидное значение порта
     */
    /*private void connectToServer() throws ConnectionErrorException, InvalidValueException {
        try {
            if (reconnectionAttempts >= 1) UserConsole.printCommandTextNext("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            UserConsole.printCommandTextNext("Соединение с сервером успешно установлено.");
            UserConsole.printCommandTextNext("Ожидание разрешения на обмен данными.");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            UserConsole.printCommandTextNext("Разрешение на обмен данными получено.");
        } catch (IllegalArgumentException ex) {
            UserConsole.printCommandError("Адрес сервера введен некорректно");
            throw new InvalidValueException();
        } catch (IOException ex) {
            UserConsole.printCommandError("Произошла ошибка соединения с сервером.");
            throw new ConnectionErrorException();
        }

    }*/

    private void connectToServer() throws ConnectionErrorException, InvalidValueException {
        try {
            UserConsole.printCommandTextNext("ConnectionToServer");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            isConnected = true;
            UserConsole.printCommandTextNext("ConnectionToServerComplete");
        } catch (IllegalArgumentException exception) {
            UserConsole.printCommandError("ServerAddressException");
            isConnected = false;
            throw new InvalidValueException();
        } catch (IOException exception) {
            exception.printStackTrace();
            UserConsole.printCommandError("ConnectionToServerException");
            isConnected = false;
            throw new ConnectionErrorException();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getUsername() {
        return user == null ? null : user.getUsername();
    }

    /**
     * Процесс запроса сервера
     */
    public void processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                if (serverResponse != null && serverResponse.getResponseCode() == ResponseCode.TOKEN_EXPIRED) {
                    processAuthentication();
                    serverResponse = null;
                    continue;
                }
                requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getResponseCode(), user) :
                        userHandler.handle(null, user);
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                UserConsole.printCommandText(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException ex) {
                UserConsole.printCommandError("Произошла ошибка при отправке данных на сервер");
            } catch (ClassNotFoundException ex) {
                UserConsole.printCommandError("Произошла ошибка при чтении полученных данных");
            } catch (IOException ex) {
                UserConsole.printCommandError("Соединение с сервером разорвано");
                try {
                    connectToServer();
                } catch (ConnectionErrorException | InvalidValueException reconnectionEx) {
                    if (requestToServer.getCommandName().equals("exit"))
                        UserConsole.printCommandTextNext("Команда не зарегистрирована на сервере");
                    else UserConsole.printCommandTextNext("Попробуйте повторить команду позже");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
    }

    private void processAuthentication() {
        Request requestToServer;
        Response serverResponse = null;
        do {
            try {
                requestToServer = authenticationHandler.handle();
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                UserConsole.printCommandText(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException ex) {
                UserConsole.printCommandError("Ошибка при отправке данных на сервер");
            } catch (ClassNotFoundException ex) {
                UserConsole.printCommandError("Произошла ошибка при чтении полученных данных");
            } catch (IOException ex) {
                UserConsole.printCommandError("Соединение с сервером разорвано");
                try {
                    connectToServer();
                } catch (ConnectionErrorException | InvalidValueException connectEx) {
                    UserConsole.printCommandError("Попробуйте авторизоваться позднее");
                }
            }
        } while (serverResponse == null || !serverResponse.getResponseCode().equals(ResponseCode.OK));
        user = serverResponse.getUser(); //юзер со свежим токеном
    }

    public Hashtable<Integer, Flat> processRequestToServer(String commandName, String commandStringArgument,
                                                           Serializable commandObjectArgument) {
        Request requestToServer = null;
        Response serverResponse = null;
        try {
            requestToServer = new Request(commandName, commandStringArgument, commandObjectArgument, user);
            serverWriter.writeObject(requestToServer);
            serverResponse = (Response) serverReader.readObject();
            if (!serverResponse.getResponseBody().isEmpty())
                OutputerUI.tryError(serverResponse.getResponseBody(), serverResponse.getResponseBodyArgs());
        } catch (InvalidClassException | NotSerializableException exception) {
            OutputerUI.error("DataSendingException");
        } catch (ClassNotFoundException exception) {
            OutputerUI.error("DataReadingException");
        } catch (IOException exception) {
            if (requestToServer.getCommandName().equals(MainWindow.EXIT_COMMAND_NAME)) return null;
            OutputerUI.error("EndConnectionToServerException");
            try {
                connectToServer();
                OutputerUI.info("ConnectionToServerComplete");
            } catch (ConnectionErrorException | InvalidValueException reconnectionException) {
                OutputerUI.info("TryCommandLater");
            }
        }
        return serverResponse == null ? null : serverResponse.getFlatCollection();
    }

    public boolean processAuthentication(String username, String password, boolean register) {
        // TODO: Переместить все в один метод (?)
        Request requestToServer = null;
        Response serverResponse = null;
        String command;
        try {
            command = register ? MainWindow.REGISTER_COMMAND_NAME : MainWindow.LOGIN_COMMAND_NAME;
            requestToServer = new Request(command, "", new User(username, password));
            if (serverWriter == null) throw new IOException();
            serverWriter.writeObject(requestToServer);
            serverResponse = (Response) serverReader.readObject();
            OutputerUI.tryError(serverResponse.getResponseBody(), serverResponse.getResponseBodyArgs());
        } catch (InvalidClassException | NotSerializableException exception) {
            OutputerUI.error("DataSendingException");
        } catch (ClassNotFoundException exception) {
            OutputerUI.error("DataReadingException");
        } catch (IOException exception) {
            OutputerUI.error("EndConnectionToServerException");
            try {
                connectToServer();
                OutputerUI.info("ConnectionToServerComplete");
            } catch (ConnectionErrorException | InvalidValueException reconnectionException) {
                OutputerUI.info("TryAuthLater");
            }
        }
        if (serverResponse != null && serverResponse.getResponseCode().equals(ResponseCode.OK)) {
            user = requestToServer.getUser();
            return true;
        }
        return false;
    }

    public boolean processScriptToServer(File scriptFile) {
        Request requestToServer = null;
        Response serverResponse = null;
        ScriptHandler scriptHandler = new ScriptHandler(scriptFile);
        do {
            try {
                requestToServer = serverResponse != null ? scriptHandler.handle(serverResponse.getResponseCode(), user) :
                        scriptHandler.handle(null, user);
                if (requestToServer == null) return false;
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                if (!serverResponse.getResponseBody().isEmpty())
                    OutputerUI.tryError(serverResponse.getResponseBody(), serverResponse.getResponseBodyArgs());
            } catch (InvalidClassException | NotSerializableException exception) {
                OutputerUI.error("DataSendingException");
            } catch (ClassNotFoundException exception) {
                OutputerUI.error("DataReadingException");
            } catch (IOException exception) {
                OutputerUI.error("EndConnectionToServerException");
                try {
                    connectToServer();
                    OutputerUI.info("ConnectionToServerComplete");
                } catch (ConnectionErrorException | InvalidValueException reconnectionException) {
                    OutputerUI.info("TryCommandLater");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return true;
    }
}
