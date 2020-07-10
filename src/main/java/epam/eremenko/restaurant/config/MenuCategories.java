package epam.eremenko.restaurant.config;

public enum MenuCategories {

    WHOLE_MENU("All"),
    CAKES("Cakes"),
    SWEETS("Sweets"),
    COOKIES("Cookies");

    private final String category;

    MenuCategories(String category){
        this.category = category;
    }

    public String get(){
        return category;
    }
}
