package epam.eremenko.restaurant.service;

import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.entity.Menu;
import epam.eremenko.restaurant.service.exception.ServiceException;

import java.util.List;

public interface MenuService {
    void add(MenuDto menuDto, String price) throws ServiceException;

    Menu get(MenuDto menuDto) throws ServiceException;

    Menu get(MenuDto menuDto, int currentPage, int recordsPerPage) throws ServiceException;

    int getQuantityOfDishPagesIntoMenu(Menu menu, int recordsPerPage);

    List<String> getCategories();

    void updateDish(MenuDto menuDto, String changeableField, String value) throws ServiceException;
}
