package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserAlreadyExistsException;
import common.exceptions.WrongArgumentException;
import common.interaction.User;
import common.utility.JWTService;
import server.utility.ResourceFactory;


public class Refresh implements Command{
    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        if (!args.isEmpty()) throw new WrongArgumentException();
        return "";
    }

    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "RefreshDescription").get();
    }
}
