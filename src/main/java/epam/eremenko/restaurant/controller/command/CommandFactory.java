package epam.eremenko.restaurant.controller.command;

public enum CommandFactory {

    CHANGE_LOCALE(new Commander().LOCALE_CHANGER),
    SIGN_UP(new Commander().REGISTER),
    SIGN_IN(new Commander().SIGN_IN_INSPECTOR),
    SIGN_OUT(new Commander().SIGN_OUTER),
    GET_ADMIN_FORM(new Commander().ADMIN_FORMS_GETTER),
    ADD_DISH(new Commander().MENU_CREATOR),
    GET_MENU(new Commander().MENU_SUPPLIER),
    ADD_TO_ORDER(new Commander().ORDER_COLLECTOR),
    GET_CUSTOMER_FORM(new Commander().CUSTOMER_FORMS_GETTER),
    USE_TLD_TAG(new Commander().USE_TLD_TAG),
    CREATE_ORDER(new Commander().ORDER_CREATOR),
    GET_REPORT(new Commander().REPORTER);


    private final Command command;

    CommandFactory(Command command) {
        this.command = command;
    }

    public Command get() {
        return command;
    }
}
