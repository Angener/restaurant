package epam.eremenko.restaurant.service.util;

import epam.eremenko.restaurant.config.MenuCategories;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.service.exception.ServiceException;

import java.util.Arrays;

public class MenuValidator {
    private final StringBuilder stringBuilder = new StringBuilder();
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final int MIN_DESCRIPTION_LENGTH = 10;

    public void validate(MenuDto menuDto, String price) throws ServiceException {
        parsePriceFromText(menuDto, price);
        checkCategory(menuDto);
        checkName(menuDto);
        checkDescription(menuDto);
        throwAnException();
    }

    public void validate(MenuDto menuDto) throws ServiceException {
        checkCategory(menuDto);
        throwAnException();
    }

    private void parsePriceFromText(MenuDto menuDto, String price) {
        try {
            menuDto.setPrice(Double.parseDouble(price.replace(",", ".")));
        } catch (NumberFormatException ex) {
            stringBuilder.append("Invalid price. ");
        }
    }

    public void checkCategory(MenuDto menuDto) {
        if (!isEligibleCategory(menuDto)) {
            stringBuilder.append("Invalid category. ");
        }
    }

    private boolean isEligibleCategory(MenuDto menuDto) {
        return Arrays.stream(MenuCategories.values())
                .anyMatch(c -> c.get().equals(menuDto.getCategory()));
    }

    private void checkName(MenuDto menuDto) {
        String name = menuDto.getName();
        if (isNameNotEligible(name)) {
            stringBuilder.append("Name is invalid");
        }
    }

    private boolean isNameNotEligible(String name) {
        return name == null || name.length() > MAX_NAME_LENGTH || name.length() < MIN_NAME_LENGTH;
    }

    private void checkDescription(MenuDto menuDto) {
        String description = menuDto.getDescription();
        if (isDescriptionNotEligible(description)) {
            stringBuilder.append("Description is invalid");
        }
    }

    private boolean isDescriptionNotEligible(String description) {
        return (description == null) || (description.length() > MAX_DESCRIPTION_LENGTH) ||
                (description.length() < MIN_DESCRIPTION_LENGTH);
    }

    private void throwAnException() throws ServiceException {
        String errorMassage = stringBuilder.toString();
        if (!errorMassage.equals("")) {
            throw new ServiceException(errorMassage);
        }
    }
}
