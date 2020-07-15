package epam.eremenko.restaurant.service;

import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;
import epam.eremenko.restaurant.entity.Order;
import epam.eremenko.restaurant.service.exception.ServiceException;

public interface OrderService {
    void addDish(OrderDto orderDto, MenuDto dish);
    void createOrder(OrderDto orderDto) throws ServiceException;
    Order get(OrderDto orderDto) throws ServiceException;
    void delete(OrderDto orderDto) throws ServiceException;
    void update(Order order) throws ServiceException;
}
