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

//        ImageDto image = new ImageDto();
//        image.setDishId(1);
//        System.out.println( imageDao.get(image));
//
//        MenuDto menu = new MenuDto();
//        menu.setCategory("All");
//        menuDao.get(menu).getMenu().forEach(System.out::println);
//
//        UserDto user = new UserDto();
//        user.setUsername("Andrew");
//        System.out.println(userDao.get(user));


//        Map<Integer, String> map = new HashMap<>();
//        map.put(1, "vaska");
//        for (Map.Entry<Integer, String> entry : map.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + " Value: "
//                    + entry.getValue());
//        }


//        List<MenuDto> dishes = new ArrayList<>();
//
//        MenuDto dish = new MenuDto();
//        dish.setId(13);
//        dish.setQuantity(132);
//        dish.setPrice(22.2);
//        dish.setAmount(dish.getPrice() * dish.getQuantity());
//
//        MenuDto dish1 = new MenuDto();
//        dish1.setId(2);
//        dish1.setQuantity(14);
//        dish1.setPrice(20);
//        dish1.setAmount(dish1.getPrice() * dish1.getQuantity());

//
//        dishes.add(dish);
//        dishes.add(dish1);

        OrderDto order = new OrderDto();

        order.setOrderId(1);
//        order.setIsApproved(true);
//        order.setIsPassed(true);
//        order.setIsCooked(true);
//        order.setIsBilled(true);
//        order.setIsPaid(true);
//        order.setDishes(dishes);
//        order.setTotalAmount(dish.getPrice() * dish.getQuantity() + dish1.getPrice() * dish1.getQuantity());
//        order.setUserId(1);

//        orderDao.add(order);

//        orderDao.update(OrderTable.IS_APPROVED, order);
//        orderDao.update(OrderTable.IS_PASSED, order);
//        orderDao.update(OrderTable.IS_COOKED, order);
//        orderDao.update(OrderTable.IS_BILLED, order);
//        orderDao.update(OrderTable.IS_PAID, order);

//        orderDao.update(OrderTable.TOTAL_AMOUNT, order);
        OrderDto porder = orderDao.get(order);
        System.out.println(porder.toString());


//        System.out.println(orderDao.get(order).toString());



    }
}
