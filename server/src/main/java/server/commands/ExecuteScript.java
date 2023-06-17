package server.commands;

import common.exceptions.WrongArgumentException;
import common.interaction.User;
import server.utility.ResourceFactory;

public class ExecuteScript implements Command{

    @Override
    public String execute(String args, Object objectArgument, User user) throws WrongArgumentException {
        return "";
    }

    @Override
    public String getDescription(User user) {
        return ResourceFactory.getStringBinding(user.getLanguage(), "ExecuteScriptDescription").get();
    }
}
