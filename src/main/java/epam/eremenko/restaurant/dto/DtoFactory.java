package epam.eremenko.restaurant.dto;

import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dao.table.OrderMenuTable;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dao.table.UserTable;
import epam.eremenko.restaurant.config.UserRoles;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public final class DtoFactory {

    private DtoFactory() {
    }

    public static MenuDto getDishDto(Map<MenuTable, String> param) {
        MenuDto dish = new MenuDto();
        dish.setId(Integer.parseInt(param.get(MenuTable.DISH_ID)));
        dish.setName(param.get(MenuTable.DISH_NAME));
        dish.setCategory(param.get(MenuTable.CATEGORY));
        dish.setDescription(param.get(MenuTable.DESCRIPTION));
        dish.setPrice(Double.parseDouble(param.get(MenuTable.PRICE)));
        return dish;
    }

    public static MenuDto getDishDto(List<MenuDto> dishes) {
        MenuDto menu = new MenuDto();
        menu.setMenu(dishes);
        return menu;
    }

    public static MenuDto getDishForOrderDto(Map<OrderMenuTable, String> orderParam,
                                             Map<MenuTable, String> menuParam) {
        MenuDto dish = new MenuDto();
        dish.setId(Integer.parseInt(orderParam.get(OrderMenuTable.DISH_ID)));
        dish.setQuantity(Integer.parseInt(orderParam.get(OrderMenuTable.QUANTITY)));
        dish.setAmount(Double.parseDouble(orderParam.get(OrderMenuTable.AMOUNT)));
        dish.setName(menuParam.get(MenuTable.DISH_NAME));
        dish.setPrice(Double.parseDouble(menuParam.get(MenuTable.PRICE)));
        return dish;
    }

    public static UserDto getUserDto(Map<UserTable, String> param) {
        UserDto user = new UserDto();
        user.setUserId(Integer.parseInt(param.get(UserTable.USER_ID)));
        user.setRole(Enum.valueOf(UserRoles.class, param.get(UserTable.ROLE).toUpperCase()));
        user.setUsername(param.get(UserTable.USERNAME));
        user.setEmail(param.get(UserTable.EMAIL));
        user.setPassword(param.get(UserTable.PASSWORD));
        user.setMobile(param.get(UserTable.MOBILE));
        user.setRegistrationDate(Timestamp.valueOf(param.get(UserTable.REGISTERED)));
        return user;
    }

    public static ImageDto getImageDto(List<String> paths) {
        ImageDto images = new ImageDto();
        images.setImages(paths);
        return images;
    }

    public static OrderDto getOrderDto(int orderId) {
        OrderDto order = new OrderDto();
        order.setOrderId(orderId);
        return order;
    }

    public static void configureOrderFields(OrderDto order, Map<OrderTable, String> param) {
        order.setTotalAmount(Double.parseDouble(param.get(OrderTable.TOTAL_AMOUNT)));
        order.setIsApproved(castToBoolean(param.get(OrderTable.IS_APPROVED)));
        order.setIsPassed(castToBoolean(param.get(OrderTable.IS_PASSED)));
        order.setIsCooked(castToBoolean(param.get(OrderTable.IS_COOKED)));
        order.setIsBilled(castToBoolean(param.get(OrderTable.IS_BILLED)));
        order.setIsPaid(castToBoolean(param.get(OrderTable.IS_PAID)));
        order.setOrderDate(Timestamp.valueOf(param.get(OrderTable.ORDERED)));
        order.setUserId(Integer.parseInt(param.get(OrderTable.USER_ID)));
    }

    private static boolean castToBoolean(String value) {
        return value.equals("1");
    }
}