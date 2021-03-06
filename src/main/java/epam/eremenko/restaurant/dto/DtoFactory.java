package epam.eremenko.restaurant.dto;

import epam.eremenko.restaurant.attribute.ReportTypes;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dao.table.OrderMenuTable;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dao.table.UserTable;
import epam.eremenko.restaurant.attribute.UserRoles;
import epam.eremenko.restaurant.entity.Menu;
import epam.eremenko.restaurant.entity.Order;
import epam.eremenko.restaurant.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
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
        user.setId(Integer.parseInt(param.get(UserTable.USER_ID)));
        user.setRole(Enum.valueOf(UserRoles.class, param.get(UserTable.ROLE).toUpperCase()));
        user.setUsername(param.get(UserTable.USERNAME));
        user.setEmail(param.get(UserTable.EMAIL));
        user.setPassword(param.get(UserTable.PASSWORD));
        user.setMobile(param.get(UserTable.MOBILE));
        user.setRegistrationDate(Timestamp.valueOf(param.get(UserTable.REGISTERED)));
        return user;
    }

    public static UserDto getUserDto(HttpServletRequest request) {
        UserDto userDto = new UserDto();
        userDto.setUsername(request.getParameter("Username").trim());
        userDto.setPassword(request.getParameter("Password").trim());
        if (request.getParameter("Email") != null) {
            userDto.setEmail(request.getParameter("Email").trim());
        }
        if (request.getParameter("Phone") != null) {
            userDto.setMobile(request.getParameter("Phone").trim());
        }
        return userDto;
    }

    public static ImageDto getImageDto(List<String> paths) {
        ImageDto images = new ImageDto();
        images.setImages(paths);
        return images;
    }

    public static void configureOrderFields(OrderDto order, Map<OrderTable, String> param) {
        defineOrdersDtoFields(order, param);
    }

    public static OrderDto getOrderDto(int orderId){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderId);
        return orderDto;
    }

    public static OrderDto getOrderDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setDishes(order.getDishes());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setUserId(order.getUserId());
        orderDto.setIsApproved(order.isApproved());
        orderDto.setIsPassed(order.isPassed());
        orderDto.setIsCooked(order.isCooked());
        orderDto.setIsBilled(order.isBilled());
        orderDto.setIsPaid(order.isPaid());
        return orderDto;
    }

    public static OrderDto getOrderDto(User user) {
        OrderDto order = new OrderDto();
        order.setUserId(user.getId());
        return order;
    }

    public static OrderDto getOrderDto(Map<OrderTable, String> param) {
        OrderDto order = new OrderDto();
        order.setId(Integer.parseInt(param.get(OrderTable.ORDER_ID)));
        defineOrdersDtoFields(order, param);
        return order;
    }

    private static void defineOrdersDtoFields(OrderDto order, Map<OrderTable, String> param) {
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

    public static MenuDto getMenuDto(String category) {
        MenuDto menuDto = new MenuDto();
        menuDto.setCategory(category);
        return menuDto;
    }

    public static MenuDto getMenuDto(HttpServletRequest request) {
        MenuDto menuDto = new MenuDto();
        menuDto.setName(request.getParameter("name").trim());
        menuDto.setCategory(request.getParameter("category").trim());
        menuDto.setDescription(request.getParameter("description").trim());
        return menuDto;
    }

    public static MenuDto getMenuDto(int dishId, int dishQuantity, double price, String name) {
        MenuDto dish = new MenuDto();
        dish.setId(dishId);
        dish.setQuantity(dishQuantity);
        dish.setPrice(price);
        dish.setName(name);
        dish.setAmount(calculateAmount(dishQuantity, price));
        return dish;
    }

    public static  MenuDto getMenuDto(Menu menu, int dishId){
        MenuDto menuDto = null;
        for(MenuDto dto : menu.getDishes()){
            if (dto.getId() == dishId){
                menuDto = dto;
            }
        }
        return menuDto;
    }

    private static double calculateAmount(int dishQuantity, double price) {
        return dishQuantity * price;
    }

    public static ReportDto getReportDto(List<OrderDto> orders) {
        ReportDto report = new ReportDto();
        report.setOrders(new ArrayList<>(orders));
        return report;
    }

    public static ReportDto getReportDto(User user, String reportType) {
        ReportDto reportDto = new ReportDto();
        reportDto.setType(Enum.valueOf(ReportTypes.class, reportType.toUpperCase()));
        reportDto.setUserId(user.getId());
        return reportDto;
    }
}