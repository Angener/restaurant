package epam.eremenko.restaurant.attribute;

public enum PageAddresses {

    MAIN("/restaurant"),
    AUTHORIZATION("/authorization"),
    MENU("/menu"),
    MENU_REGISTER("/WEB-INF/view/menuRegister.jsp"),
    ORDER_PROCESSOR("/WEB-INF/view/orderProcessor.jsp"),
    ORDERS("/orders"),
    ORDER_CARD("/WEB-INF/view/orderCard.jsp");

    private final String path;

    PageAddresses(String path) {
        this.path = path;
    }

    public String get() {
        return path;
    }
}