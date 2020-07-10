package epam.eremenko.restaurant.util;

import epam.eremenko.restaurant.config.UserRoles;
import epam.eremenko.restaurant.dto.UserDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    







    private UserRoles role;
    @NotNull(message = "Username must be given")
    @Size(min = 2, message = "Username minimal length is 2 letter")
    private String username;
    @NotNull(message = "Email must be set")
    @Email()
    private String email;
    @NotNull(message = "Password must be set.")
    private String password;
    @NotNull
    @Pattern(regexp = "^((8|\\+3)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = "Invalid phone number")
    private String mobile;
    private Timestamp registrationDate;


}
