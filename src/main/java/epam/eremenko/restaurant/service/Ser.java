package epam.eremenko.restaurant.service;

import epam.eremenko.restaurant.dao.*;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.impl.OrderDao;
import epam.eremenko.restaurant.dao.table.ImageTable;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dao.table.UserTable;
import epam.eremenko.restaurant.dto.*;

import java.util.*;


public class Ser {
    public static void main(String[] args) throws DaoException {
        Dao<UserDto, UserTable> userDao = DaoFactory.getInstance().getUserDao();
        Dao<MenuDto, MenuTable> menuDao = DaoFactory.getInstance().getMenuDao();
        Dao<OrderDto, OrderTable> orderDao = DaoFactory.getInstance().getOrderDao();
        Dao<ImageDto, ImageTable> imageDao = DaoFactory.getInstance().getImageDao();


    }
}
