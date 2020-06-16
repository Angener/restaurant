package epam.eremenko.restaurant.dao.table;

public enum UserTable {

    TABLE_NAME("`user`"),
    USER_ID("user_id"),
    ROLE("role"),
    USERNAME("username"),
    EMAIL("email"),
    PASSWORD("password"),
    MOBILE("mobile"),
    REGISTERED("registered");

    private final String value;

    UserTable(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
