package epam.eremenko.restaurant.attribute;

public enum PageAddresses {

    MAIN("/restaurant"),
    AUTHORIZATION("/authorization"),
    MENU("/menu"),
    ORDERS("/orders"),
    MENU_REGISTER("/WEB-INF/view/menuRegister.jsp"),
    MENU_EDITOR("/WEB-INF/view/menuEditor.jsp"),
    ORDER_PROCESSOR("/WEB-INF/view/orderProcessor.jsp"),
    ORDER_CARD("/WEB-INF/view/orderCard.jsp"),
    BILL("/WEB-INF/view/bill.jsp");

    private final String path;

    PageAddresses(String path) {
        this.path = path;
    }

    public String get() {
        return path;
    }
}