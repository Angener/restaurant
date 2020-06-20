package epam.eremenko.restaurant.dao.table;

public enum ImageTable {
    TABLE_NAME("`image`"),
    IMAGE_ID("image_id"),
    IMAGE_NAME("image_name"),
    IMAGE_PATH("image_path"),
    DISH_ID("dish_id");

    private final String value;

    ImageTable(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
