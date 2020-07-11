package epam.eremenko.restaurant.service.impl;

import epam.eremenko.restaurant.service.MenuService;
import epam.eremenko.restaurant.service.OrderService;
import epam.eremenko.restaurant.service.UserService;

public final class ServiceFactory {
    private static ServiceFactory instance;
    private static final MenuService MENU_SERVICE = new MenuServiceImpl();
    private static final UserService USER_SERVICE = new UserServiceImpl();
    private static final OrderService ORDER_SERVICE = new OrderServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            synchronized (ServiceFactory.class) {
                if (instance == null) {
                    instance = new ServiceFactory();
                }
            }
        }
        return instance;
    }

    public MenuService getMenuService() {
        return MENU_SERVICE;
    }

    public UserService getUserService() {
        return USER_SERVICE;
    }

    public OrderService getOrderService(){
        return ORDER_SERVICE;
    }
}
