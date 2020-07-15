package epam.eremenko.restaurant.entity;

import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;
import epam.eremenko.restaurant.dto.ReportDto;
import epam.eremenko.restaurant.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class BeanFactory {
    private static BeanFactory instance;

    private BeanFactory() {
    }

    public static BeanFactory getInstance() {
        if (instance == null) {
            synchronized (BeanFactory.class) {
                if (instance == null) {
                    instance = new BeanFactory();
                }
            }
        }
        return instance;
    }

    public User getUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setMobile(userDto.getMobile());
        user.setRegistrationDate(userDto.getRegistrationDate());
        user.setRole(userDto.getRole());
        return user;
    }

    public Menu getMenu() {
        return new Menu();
    }

    public Menu getMenu(List<MenuDto> menuDto) {
        return new Menu(menuDto);
    }

    public Order getOrder(OrderDto orderDto) {
        return new Order(orderDto);
    }

    public Report getReport(ReportDto reportDto) {
        List<Order> orders = new ArrayList<>();
        if (reportDto.getOrders().size() != 0) {
            orders = reportDto.getOrders().stream()
                    .map(this::getOrder)
                    .collect(Collectors.toList());


        }
        return new Report(orders);
    }
}
