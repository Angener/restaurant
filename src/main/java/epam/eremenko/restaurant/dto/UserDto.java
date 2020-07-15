package epam.eremenko.restaurant.dto;

import epam.eremenko.restaurant.attribute.UserRoles;

import java.sql.Timestamp;

public class UserDto extends Dto {
    private UserRoles role;
    private String username;
    private String email;
    private String password;
    private String mobile;
    private Timestamp registrationDate;

    public UserDto() {
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + getId() +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
