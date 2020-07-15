package epam.eremenko.restaurant.attribute;

public enum UserRoles {
    ADMIN("admin"),
    CUSTOMER("customer");

    private final String role;

    UserRoles(String role){
        this.role = role;
    }

    public String get() {
        return role;
    }
}
