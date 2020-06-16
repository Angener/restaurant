package epam.eremenko.restaurant.dao.table;

public enum OrderMenuTable {
    TABLE_NAME("`order_has_menu`"),
    ORDER_ID("order_id"),
    DISH_ID("dish_id"),
    QUANTITY("quantity"),
    AMOUNT("amount");

    private String value;

    OrderMenuTable(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
