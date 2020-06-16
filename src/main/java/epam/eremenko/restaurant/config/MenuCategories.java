package epam.eremenko.restaurant.config;

public enum MenuCategories {

    WHOLE_MENU("All"),
    DELICACIES("Деликатесы"),
    BELARUSIAN_GOLD("Золото Беларуси");


    private final String category;

    MenuCategories(String category){
        this.category = category;
    }

    public String get(){
        return category;
    }
}
