package epam.eremenko.restaurant.controller.command;

public enum CommandFactory {

    CHANGE_LOCALE(new CommandImpl().LOCALE_CHANGER),
    SIGN_UP(new CommandImpl().REGISTER),
    SIGN_IN(new CommandImpl().SIGN_IN_INSPECTOR),
    SIGN_OUT(new CommandImpl().SIGN_OUTER),
    GET_ADMIN_FORM(new CommandImpl().ADMIN_FORMS_GETTER),
    ADD_DISH(new CommandImpl().MENU_CREATOR),
    GET_MENU(new CommandImpl().MENU_SUPPLIER),
    ADD_TO_ORDER(new CommandImpl().ORDER_COLLECTOR),
    GET_CUSTOMER_FORM(new CommandImpl().CUSTOMER_FORMS_GETTER),
    USE_TLD_TAG(new CommandImpl().USE_TLD_TAG),
    CREATE_ORDER(new CommandImpl().ORDER_CREATOR);


    private final Command command;

    CommandFactory(Command command) {
        this.command = command;
    }

    public Command get() {
        return command;
    }
}
