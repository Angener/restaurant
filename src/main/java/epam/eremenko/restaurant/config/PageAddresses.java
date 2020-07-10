package epam.eremenko.restaurant.config;

public enum PageAddresses {

    MAIN("/restaurant"),
    AUTHORIZATION("/authorization"),
    MENU("/menu"),
    MENU_REGISTER("/menuRegister");

    private final String path;

    PageAddresses(String path) {
        this.path = path;
    }

    public String get() {
        return path;
    }
}
