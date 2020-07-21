package epam.eremenko.restaurant.service.util;

import epam.eremenko.restaurant.dto.UserDto;
import epam.eremenko.restaurant.service.exception.ServiceException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class UserValidator {

    private static Pattern pattern;
    private static Matcher matcher;

    private final StringBuilder stringBuilder = new StringBuilder();
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MOBILE_PATTERN =
            "^((8|\\+3)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MIN_USERNAME_LENGTH = 2;
    private static final int MAX_USERNAME_LENGTH = 49;


    public void validate(UserDto userDto) throws ServiceException {
        resetStringBuilder();
        validateEmail(userDto);
        validatePhone(userDto);
        validatePassword(userDto);
        validateUsername(userDto);
        throwAnException();
    }

    private void resetStringBuilder(){
        stringBuilder.delete(0, stringBuilder.length());
    }

    private void validateEmail(UserDto userDto) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(userDto.getEmail());
        if (!matcher.matches()) {
            stringBuilder.append("message.error.singIn.email ");
        }
    }

    private void validatePassword(UserDto userDto) {
        if (userDto.getPassword().length() < MIN_PASSWORD_LENGTH) {
            stringBuilder.append("message.error.signIn.passwordLength ");
        }
    }

    private void validatePhone(UserDto userDto) {
        pattern = Pattern.compile(MOBILE_PATTERN);
        matcher = pattern.matcher(userDto.getMobile());
        if (!matcher.matches()) {
            stringBuilder.append("message.error.signIn.phone ");
        }
    }

    private void validateUsername(UserDto userDto) {
        if (userDto.getUsername().length() <= MIN_USERNAME_LENGTH ||
                userDto.getUsername().length() >= MAX_USERNAME_LENGTH) {
            stringBuilder.append("message.error.signIn.usernameLength ");
        }
    }

    private void throwAnException() throws ServiceException {
        String errorMassage = stringBuilder.toString();
        if (!errorMassage.equals("")) {
            throw new ServiceException(errorMassage);
        }
    }
}
