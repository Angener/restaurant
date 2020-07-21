package epam.eremenko.restaurant.service.util;

import epam.eremenko.restaurant.attribute.MenuCategories;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.service.exception.ServiceException;

import java.util.Arrays;

public final class MenuValidator {
    private final StringBuilder stringBuilder = new StringBuilder();
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final int MIN_DESCRIPTION_LENGTH = 10;

    public void validate(MenuDto menuDto, String price) throws ServiceException {
        resetErrors();
        parsePriceFromText(menuDto, price);
        checkCategory(menuDto.getCategory());
        checkName(menuDto.getName());
        checkDescription(menuDto.getDescription());
        throwAnException();
    }

    public void resetErrors(){
        stringBuilder.delete(0, stringBuilder.length());
    }

    public void validate(MenuDto menuDto) throws ServiceException {
        resetErrors();
        checkCategory(menuDto.getCategory());
        throwAnException();
    }

    private void parsePriceFromText(MenuDto menuDto, String price) {
        try {
            menuDto.setPrice(Double.parseDouble(price.replace(",", ".")));
        } catch (NumberFormatException ex) {
            stringBuilder.append("message.error.menu.price ");
        }
    }

    public void checkPrice (String price) {
        try {
           Double.parseDouble(price.replace(",", "."));
        } catch (NumberFormatException ex) {
            stringBuilder.append("message.error.menu.price ");
        }
    }

    public void checkCategory(String category) {
        if (!isEligibleCategory(category)) {
            stringBuilder.append("message.error.menu.category ");
        }
    }

    private boolean isEligibleCategory(String category) {
        return Arrays.stream(MenuCategories.values())
                .anyMatch(c -> c.get().equals(category));
    }

    public void checkName(String name) {
        if (isNameNotEligible(name)) {
            stringBuilder.append("message.error.menu.name ");
        }
    }

    private boolean isNameNotEligible(String name) {
        return name == null || name.length() > MAX_NAME_LENGTH || name.length() < MIN_NAME_LENGTH;
    }

    public void checkDescription(String description) {
        if (isDescriptionNotEligible(description)) {
            stringBuilder.append("message.error.menu.description ");
        }
    }

    private boolean isDescriptionNotEligible(String description) {
        return (description == null) || (description.length() > MAX_DESCRIPTION_LENGTH) ||
                (description.length() < MIN_DESCRIPTION_LENGTH);
    }

    public void throwAnException() throws ServiceException {
        String errorMassage = stringBuilder.toString();
        if (!errorMassage.equals("")) {
            throw new ServiceException(errorMassage);
        }
    }
}
