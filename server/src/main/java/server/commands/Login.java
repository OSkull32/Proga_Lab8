package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserAlreadyExistsException;
import common.exceptions.UserIsNotFoundException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.JWTService;
import server.utility.DatabaseUserManager;
import server.utility.ResourceFactory;

public class Login implements Command{
    private final DatabaseUserManager databaseUserManager;

    public Login(DatabaseUserManager databaseUserManager) {
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        var lang = user.getLanguage();
        try {
            if (databaseUserManager.checkUserByUsernameAndPassword(user)) {
                builder.append(ResourceFactory.getStringBinding(lang, "LoginDone").get());
                user.setToken(JWTService.generateToken(user.getUsername()));
            } else throw new UserIsNotFoundException();
        } catch (ClassCastException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "LoginWrongObject").get()).append("\n");
        } catch (DatabaseHandlingException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "LoginErrorDatabase").get()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "LoginDescription").get();
    }
}
