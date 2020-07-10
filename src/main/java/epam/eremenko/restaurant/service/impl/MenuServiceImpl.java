package epam.eremenko.restaurant.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);
    private static final Dao<MenuDto, MenuTable> MENU_DAO = DaoFactory.getInstance().getMenuDao();

    public void add(MenuDto menuDto, String price) throws ServiceException {
        validate(menuDto, price);
        addToDatabase(menuDto);
    }

    private void validate(MenuDto menuDto, String price) throws ServiceException {
        new MenuValidator().validate(menuDto, price);
    }

    private void addToDatabase(MenuDto menuDto) throws ServiceException {
        try {
            MENU_DAO.add(menuDto);
        } catch (DaoException ex) {
            LOGGER.debug(ex.getMessage());
            throw new ServiceException("Dish already exists");
        }
    }

    public Menu get(MenuDto menuDto) throws ServiceException {
        new MenuValidator().validate(menuDto);
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

    public int getQuantityOfDishPagesIntoMenu(Menu menu, int recordsPerPage) {
        int dishesQuantity = menu.getDishesQuantity();
        return (int) Math.ceil(dishesQuantity * 1.0 / recordsPerPage);
    }
}
