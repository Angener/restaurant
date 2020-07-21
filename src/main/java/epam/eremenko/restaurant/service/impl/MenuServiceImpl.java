package epam.eremenko.restaurant.service.impl;

import epam.eremenko.restaurant.attribute.MenuCategories;
import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.impl.DaoFactory;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.entity.BeanFactory;
import epam.eremenko.restaurant.entity.Menu;
import epam.eremenko.restaurant.service.MenuService;
import epam.eremenko.restaurant.service.exception.ServiceException;
import epam.eremenko.restaurant.service.util.MenuValidator;
import epam.eremenko.restaurant.service.util.UtilFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);
    private static final Dao<MenuDto, MenuTable> MENU_DAO = DaoFactory.getInstance().getMenuDao();
    private final MenuValidator menuValidator = UtilFactory.getInstance().getMenuValidator();

    @Override
    public void add(MenuDto menuDto, String price) throws ServiceException {
        validate(menuDto, price);
        addToDatabase(menuDto);
    }

    private void validate(MenuDto menuDto, String price) throws ServiceException {
        menuValidator.validate(menuDto, price);
    }

    private void addToDatabase(MenuDto menuDto) throws ServiceException {
        try {
            MENU_DAO.add(menuDto);
        } catch (DaoException ex) {
            LOGGER.debug(ex.getMessage());
            throw new ServiceException("message.error.menu.dishExists");
        }
    }

    @Override
    public Menu get(MenuDto menuDto) throws ServiceException {
        menuValidator.validate(menuDto);
        menuDto = collectMenu(menuDto);
        return BeanFactory.getInstance().getMenu(menuDto.getMenu());
    }

    private MenuDto collectMenu(MenuDto menuDto) throws ServiceException {
        try {
            return MENU_DAO.get(menuDto);
        } catch (DaoException ex) {
            LOGGER.debug(ex.toString());
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public Menu get(MenuDto menuDto, int currentPage, int recordsPerPage) throws ServiceException {
        Menu menu = get(menuDto);
        shortDishesList(menu, currentPage, recordsPerPage);
        return menu;
    }

    private void shortDishesList(Menu menu, int currentPage, int recordsPerPage) {
        int startPosition = (currentPage - 1) * recordsPerPage;
        int threshold = setThreshold(menu.getDishes().size(), startPosition, recordsPerPage);
        menu.setDishes(collectPartMenuDishes(menu, startPosition, threshold));
    }

    private int setThreshold(int menuSize, int startPosition, int recordsPerPage) {
        int threshold = startPosition + recordsPerPage;
        return Math.min(menuSize, threshold);
    }

    private List<MenuDto> collectPartMenuDishes(Menu menu, int startPosition, int threshold) {
        List<MenuDto> result = new ArrayList<>();
        for (int i = startPosition; i < threshold; i++) {
            result.add(menu.getDishes().get(i));
        }
        return result;
    }

    @Override
    public int getQuantityOfDishPagesIntoMenu(Menu menu, int recordsPerPage) {
        int dishesQuantity = menu.getDishesQuantity();
        return (int) Math.ceil(dishesQuantity * 1.0 / recordsPerPage);
    }

    @Override
    public List<String> getCategories() {
        List<String> categories = Arrays.stream(MenuCategories.values())
                .map(MenuCategories::get)
                .collect(Collectors.toList());
        categories.removeIf(c -> c.equals("All"));
        return categories;
    }

    @Override
    public void updateDish(MenuDto menuDto, String changeableField, String value) throws ServiceException {
        setNewValueOfDishToMenuDto(menuDto, changeableField, value);
        updateDishInDatabase(MenuTable.valueOf(changeableField), menuDto);
        LOGGER.debug("Dish: " + menuDto.getId() + "; field: " + changeableField + " updated.");
    }

    private void setNewValueOfDishToMenuDto(MenuDto menuDto, String changeableField,
                                            String value) throws ServiceException {
        switch (MenuTable.valueOf(changeableField)) {
            case DISH_NAME -> setNewDishName(menuDto, value);
            case CATEGORY -> setNewDishCategory(menuDto, value);
            case DESCRIPTION -> setNewDishDescription(menuDto, value);
            case PRICE -> setNewDishPrice(menuDto, value);
        }
    }

    private void setNewDishName(MenuDto menuDto, String name) throws ServiceException{
        menuValidator.resetErrors();
        menuValidator.checkName(name);
        menuValidator.throwAnException();
        menuDto.setName(name);
    }

    private void setNewDishCategory(MenuDto menuDto, String category) throws ServiceException{
        menuValidator.resetErrors();
        menuValidator.checkCategory(category);
        menuValidator.throwAnException();
        menuDto.setCategory(category);
    }

    private void setNewDishDescription(MenuDto menuDto, String description) throws ServiceException{
        menuValidator.resetErrors();
        menuValidator.checkDescription(description);
        menuValidator.throwAnException();
        menuDto.setDescription(description);
    }

    private void setNewDishPrice(MenuDto menuDto, String price) throws ServiceException{
        menuValidator.resetErrors();
        menuValidator.checkPrice(price);
        menuValidator.throwAnException();
        menuDto.setPrice(Double.parseDouble(price.replace(",", ".")));
    }

    private void updateDishInDatabase(MenuTable changeableField, MenuDto menuDto) throws ServiceException {
        try {
            MENU_DAO.update(changeableField, menuDto);
        } catch (DaoException ex) {
            LOGGER.debug(ex.toString());
            throw new ServiceException(ex.getMessage());
        }
    }
}
