package epam.eremenko.restaurant.controller.command;

public enum CommandFactory {

    CHANGE_LOCALE(new CommandImpl().LOCALE_CHANGER),
    SIGN_UP(new CommandImpl().REGISTER),
    SIGN_IN(new CommandImpl().SIGN_IN_INSPECTOR),
    SIGN_OUT(new CommandImpl().SIGN_OUTER),
    GET_FORM(new CommandImpl().MENU_FORMATTER),
    ADD_DISH(new CommandImpl().MENU_CREATOR),
    GET_MENU(new CommandImpl().MENU_SUPPLIER);


    private final Command command;

    CommandFactory(Command command) {
        this.command = command;
    }

    public Command get() {
        return command;
    }
}
