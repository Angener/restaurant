package epam.eremenko.restaurant.config;

public enum Parameters {

    USERNAME("username"),
    EMAIL("email"),
    PASSWORD("password"),
    MOBILE("mobile");

    private final String parameter;

    Parameters(String parameter){
        this.parameter = parameter;
    }

    public String get(){
        return parameter;
    }
}
