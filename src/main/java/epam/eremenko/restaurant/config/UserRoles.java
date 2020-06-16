package epam.eremenko.restaurant.config;

public enum UserRoles {
    ADMIN("admin"),
    CUSTOMER("customer");

    private String role;

    UserRoles(String role){
        this.role = role;
    }
}
