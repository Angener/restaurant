package epam.eremenko.restaurant.config;

public enum PageAddresses {

    MAIN("/restaurant"),
    AUTHORIZATION("/authorization"),
    MENU("/menu"),
    MENU_REGISTER("/WEB-INF/view/menuRegister.jsp"),
    ORDER_PROCESSOR("/WEB-INF/view/orderProcessor.jsp");

    private final String path;

    PageAddresses(String path) {
        this.path = path;
    }

    public String get() {
        return path;
    }
}