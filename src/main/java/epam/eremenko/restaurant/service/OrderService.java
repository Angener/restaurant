package epam.eremenko.restaurant.service;

import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;

public interface OrderService {
    void addDish(OrderDto order, MenuDto dish);
}
