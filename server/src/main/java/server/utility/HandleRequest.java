package server.utility;

import common.data.Flat;
import common.exceptions.*;
import common.interaction.User;
import common.interaction.requests.Request;
import common.interaction.responses.Response;
import common.interaction.responses.ResponseCode;
import common.utility.JWTService;
import server.App;
import server.commands.CommandManager;

import java.util.Hashtable;

public class HandleRequest {
    private final CommandManager commandManager;
    private CollectionManager collectionManager;
    private final ServerConsole serverConsole; //todo заменить на возврит стринга из метода

    public HandleRequest(CommandManager commandManager, ServerConsole serverConsole, CollectionManager collectionManager) {
        this.commandManager = commandManager;
        this.serverConsole = serverConsole;
        this.collectionManager = collectionManager;

    }

    public Client handle (Client client) {
        Request request = client.getRequest();
        String answer;
        ResponseCode responseCode;
        User user = request.getUser();

        //этот try выставляет responseCode и responseBody (answer)
        try {
            user = adaptUser(user, request.getLanguage());
            answer = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                    request.getCommandObjectArgument(), user);
//            if (!answer.isEmpty()) {
//                responseCode = ResponseCode.OK;
//            } else {
//                responseCode = ResponseCode.REFRESH;
//            }
            responseCode = ResponseCode.OK;
        } catch (InvalidCommandException | WrongArgumentException e) {
            App.logger.warning("Ошибка " + e.getClass() + " при попытке исполнить команду: " + request.getCommandName());
            answer = "";
            responseCode = ResponseCode.ERROR;
        } catch (UserIsNotFoundException | UserAlreadyExistsException ex) {
            answer = ex.getMessage();
            responseCode = ResponseCode.ERROR;
        } catch (TokenException e) {
            answer = "Токен всё. Нужна повторная авторизация\n";
            responseCode = ResponseCode.TOKEN_EXPIRED;
        }
        int clientHash = request.getCollectionHashCode();
        int serverHash = collectionManager.getCollection().hashCode();
        Hashtable<Integer, Flat> collectionToResponse;
        if (clientHash == serverHash || clientHash == 0) {
            collectionToResponse = null;
            System.out.println("null");
        } else {
            collectionToResponse = (Hashtable<Integer, Flat>) collectionManager.getCollection().clone();
            System.out.println("not null");
        }

        client.setServerResponse(new Response(responseCode, answer, new User(null, null).setToken(user.getToken()), null, collectionToResponse));
        return client;
    }

    /**
     * Выполняет команду из запроса
     *
     * @param command               имя команды
     * @param commandStringArgument String аргумент команды
     * @param commandObjectArgument Object аргумента команды
     * @return статус исполнения
     */
    private String executeCommand(String command, String commandStringArgument, Object commandObjectArgument, User user)
            throws InvalidCommandException, WrongArgumentException, UserAlreadyExistsException, UserIsNotFoundException {
        return commandManager.executeCommand(command, commandStringArgument, commandObjectArgument, user);
    }

    //после этой штуки юзер будет таким, что наш старый сервер сможет с ним работать
    private User adaptUser(User user, String language) throws TokenException {
        if (user.getToken() != null) {
            if (JWTService.verifyToken(user.getToken())) {
                return new User(JWTService.getUsername(user.getToken()), "").setLanguage(language);
            } else {
                throw new TokenException();
            }
        } else if (user.getUsername() != null && user.getPassword() != null) {
            return new User(user.getUsername(), PasswordHasher.hashPassword(user.getPassword())).setLanguage(language); //бывш. hashed user
        } else {
            throw new UserIsNotFoundException("слишком много null-ов");
        }
    }
}
