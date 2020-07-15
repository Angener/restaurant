package epam.eremenko.restaurant.service.impl;

import epam.eremenko.restaurant.service.MenuService;
import epam.eremenko.restaurant.service.OrderService;
import epam.eremenko.restaurant.service.ReportService;
import epam.eremenko.restaurant.service.UserService;

public final class ServiceFactory {
    private static ServiceFactory instance;
    private final MenuService MENU_SERVICE = new MenuServiceImpl();
    private final UserService USER_SERVICE = new UserServiceImpl();
    private final OrderService ORDER_SERVICE = new OrderServiceImpl();
    private final ReportService REPORT_SERVICE = new ReportServiceImpl();

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

    public ReportService getReportService(){
        return REPORT_SERVICE;
    }
}
