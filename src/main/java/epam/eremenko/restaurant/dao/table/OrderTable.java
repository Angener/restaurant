package epam.eremenko.restaurant.dao.table;

public enum OrderTable {
    TABLE_NAME("`order`"),
    ORDER_ID("order_id"),
    ORDERED("ordered"),
    TOTAL_AMOUNT("total_amount"),
    IS_APPROVED("is_approved"),
    IS_PASSED("is_passed"),
    IS_COOKED("is_cooked"),
    IS_BILLED("is_billed"),
    IS_PAID("is_paid"),
    USER_ID("user_id");

    private final String value;

    OrderTable(String value) {
        this.value = value;
    }

    public String get(){
        return value;
    }
}
