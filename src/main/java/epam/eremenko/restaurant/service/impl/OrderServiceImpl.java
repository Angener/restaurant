package epam.eremenko.restaurant.service.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.impl.DaoFactory;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;
import epam.eremenko.restaurant.service.OrderService;
import epam.eremenko.restaurant.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final Dao<OrderDto, OrderTable> ORDER_DAO = DaoFactory.getInstance().getOrderDao();

    @Override
    public void addDish(OrderDto orderDto, MenuDto dish) {
        if (isOrderDoesNotContainAnyDishes(orderDto)) {
            openOrder(orderDto);
            addDishAsNewPosition(orderDto, dish);
        } else {
            addDishToExistsOrder(orderDto, dish);
        }
    }

    private void addDishToExistsOrder(OrderDto order, MenuDto dish) {
        if (isDishAlreadyPresentIntoOrder(order, dish)) {
            addToExistsDish(order, dish);
        } else {
            addNewDish(order, dish);
        }
    }

    private boolean isOrderDoesNotContainAnyDishes(OrderDto orderDto) {
        return orderDto.getDishes() == null;
    }

    private void openOrder(OrderDto orderDto) {
        orderDto.setDishes(new ArrayList<>());
    }

    private void addDishAsNewPosition(OrderDto orderDto, MenuDto dish) {
        orderDto.getDishes().add(dish);
        increaseOrderTotalAmount(orderDto, dish);
    }

    private void increaseOrderTotalAmount(OrderDto orderDto, MenuDto dish) {
        orderDto.setTotalAmount(getRoundAmount(orderDto.getTotalAmount(), dish.getAmount()));
        LOGGER.debug("User " + orderDto.getUserId() + " has added the dish: " + dish.getId() + " to order");
    }

    private double getRoundAmount(double x, double y) {
        return (double) Math.round((x + y) * 100d) / 100d;
    }

    private boolean isDishAlreadyPresentIntoOrder(OrderDto orderDto, MenuDto dish) {
        return orderDto.getDishes().stream()
                .anyMatch(d -> d.getId() == dish.getId());
    }

    private void addToExistsDish(OrderDto orderDto, MenuDto dish) {
        orderDto.getDishes().stream()
                .parallel()
                .filter(d -> d.getId() == dish.getId())
                .forEach(d -> {
                    d.setQuantity(d.getQuantity() + dish.getQuantity());
                    d.setAmount(getRoundAmount(d.getAmount(), dish.getAmount()));
                });
        increaseOrderTotalAmount(orderDto, dish);
    }

    private void addNewDish(OrderDto orderDto, MenuDto dish) {
        addDishAsNewPosition(orderDto, dish);
    }

    public void createOrder(OrderDto orderDto) throws ServiceException {
        try {
            ORDER_DAO.add(orderDto);
        } catch (DaoException ex) {
            handleException(ex);
        }
    }

    private void handleException(DaoException ex) throws ServiceException {
        LOGGER.debug(ex.toString());
        throw new ServiceException(ex.getMessage());
    }
}
