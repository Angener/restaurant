package epam.eremenko.restaurant.service.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.impl.DaoFactory;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;
import epam.eremenko.restaurant.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final Dao<OrderDto, OrderTable> ORDER_DAO = DaoFactory.getInstance().getOrderDao();


    @Override
    public void addDish(OrderDto order, MenuDto dish) {
        if (isOrderDoesNotContainAnyDishes(order)) {
            openOrder(order);
            addDishAsNewPosition(order, dish);
        } else {
            addDishToExistsOrder(order, dish);
        }
    }

    private void addDishToExistsOrder(OrderDto order, MenuDto dish) {
        if (isDishAlreadyPresentIntoOrder(order, dish)) {
            addToExistsDish(order, dish);
        } else {
            addNewDish(order, dish);
        }
    }

    private boolean isOrderDoesNotContainAnyDishes(OrderDto order) {
        return order.getDishes() == null;
    }

    private void openOrder(OrderDto order) {
        order.setDishes(new ArrayList<>());
    }

    private void addDishAsNewPosition(OrderDto order, MenuDto dish) {
        order.getDishes().add(dish);
        increaseOrderTotalAmount(order, dish);
    }

    private void increaseOrderTotalAmount(OrderDto order, MenuDto dish) {
        order.setTotalAmount(getRoundAmount(order.getTotalAmount(), dish.getAmount()));
        LOGGER.debug("User " + order.getUserId() + " has added the dish: " + dish.getId() + " to order");
    }

    private double getRoundAmount(double x, double y){
        return (double)Math.round((x + y) * 100d)/100d;
    }

    private boolean isDishAlreadyPresentIntoOrder(OrderDto order, MenuDto dish) {
        return order.getDishes().stream()
                .anyMatch(d -> d.getId() == dish.getId());
    }

    private void addToExistsDish(OrderDto order, MenuDto dish) {
        order.getDishes().stream()
                .parallel()
                .filter(d -> d.getId() == dish.getId())
                .forEach(d -> {
                    d.setQuantity(d.getQuantity() + dish.getQuantity());
                    d.setAmount(getRoundAmount(d.getAmount(), dish.getAmount()));
                });
        increaseOrderTotalAmount(order, dish);
    }

    private void addNewDish(OrderDto order, MenuDto dish) {
        addDishAsNewPosition(order, dish);
    }
}
