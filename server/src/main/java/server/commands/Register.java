package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserAlreadyExistsException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.JWTService;
import server.utility.DatabaseUserManager;
import server.utility.ResourceFactory;

public class Register implements Command{
    private DatabaseUserManager databaseUserManager;

    public Register(DatabaseUserManager databaseUserManager) {
        this.databaseUserManager = databaseUserManager;
    }
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        var builder = new StringBuilder();
        var lang = user.getLanguage();
        try {
            if (databaseUserManager.insertUser(user)) {
            builder.append(ResourceFactory.getStringBinding(lang, "RegisterDone").get()).append("\n");
            user.setToken(JWTService.generateToken(user.getUsername()));
        } else throw new UserAlreadyExistsException();
        } catch (ClassCastException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "RegisterWrongObject").get()).append("\n");
        } catch (DatabaseHandlingException ex) {
            builder.append(ResourceFactory.getStringBinding(lang, "RegisterErrorDatabase").get()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "RegisterDescription").get();
    }
}
