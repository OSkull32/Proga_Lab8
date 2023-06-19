package client;

import client.gui.MainWindow;
import client.utility.OutputerUI;
import client.utility.ScriptHandler;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Класс запускающий клиент
 */
public class Client implements Runnable{
    private final String host;
    private final int port;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private User user;
    private boolean isConnected;
    private String currentLanguage;
    private final List<String> historyList;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        historyList = new LinkedList<>();
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
            //exception.printStackTrace();
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

    public Hashtable<Integer, Flat> processRequestToServer(String commandName, String commandStringArgument,
                                                           Serializable commandObjectArgument) {
        Request requestToServer = null;
        Response serverResponse = null;
        try {
            requestToServer = new Request(commandName, commandStringArgument, commandObjectArgument, user, currentLanguage);
            serverWriter.writeObject(requestToServer);
            serverResponse = (Response) serverReader.readObject();
            if (!serverResponse.getResponseBody().isEmpty()) {
                OutputerUI.tryError(serverResponse.getResponseBody(), serverResponse.getResponseBodyArgs());
                addToHistory(commandName);
            }
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
            requestToServer = new Request(command, "", null, new User(username, password), currentLanguage);
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
        ScriptHandler scriptHandler = new ScriptHandler(scriptFile, currentLanguage);
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

    public void setLanguage(String language) {
        this.currentLanguage = language;
    }

    public void addToHistory(String commandName) {
        historyList.add(commandName);
        if (historyList.size() > 13)
            historyList.remove(0);
    }

    public List<String> getHistoryList() {
        return historyList;
    }
}
