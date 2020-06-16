package epam.eremenko.restaurant.dao.table;

public enum MenuTable {

    TABLE_NAME("`menu`"),
    DISH_ID ("dish_id"),
    DISH_NAME("dish_name"),
    CATEGORY("category"),
    DESCRIPTION("description"),
    PRICE("price");

    private final String value;

    MenuTable(String value){this.value = value;}

    public String get(){
        return value;
    }
}
