package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.table.ImageTable;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dao.table.UserTable;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.ImageDto;
import epam.eremenko.restaurant.dto.OrderDto;
import epam.eremenko.restaurant.dto.ReportDto;
import epam.eremenko.restaurant.dto.UserDto;

public final class DaoFactory {
    private static volatile DaoFactory instance;
    private final Dao<UserDto, UserTable> userDao = new UserDao();
    private final Dao<MenuDto, MenuTable> menuDao = new MenuDao();
    private final Dao<OrderDto, OrderTable> orderDao = new OrderDao();
    private final Dao<ImageDto, ImageTable> imageDao = new ImageDao();
//    private final ReporterDao<ReportDto> reporter = new OrderReporter();
    private final Dao<ReportDto, OrderTable> reporter = new OrderReporter();


    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (instance == null) {
            synchronized (DaoFactory.class) {
                if (instance == null) {
                    instance = new DaoFactory();
                }
            }
        }
        return instance;
    }

    public Dao<UserDto, UserTable> getUserDao() {
        return userDao;
    }

    public Dao<MenuDto, MenuTable> getMenuDao() {
        return menuDao;
    }

    public Dao<OrderDto, OrderTable> getOrderDao() {
        return orderDao;
    }

    public Dao<ImageDto, ImageTable> getImageDao() {
        return imageDao;
    }

//    public ReporterDao<ReportDto> getReporter() {
//        return reporter;
//    }

    public Dao<ReportDto, OrderTable> getReporter() {
        return reporter;
    }
}
