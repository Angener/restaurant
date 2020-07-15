package epam.eremenko.restaurant.controller.command;


import epam.eremenko.restaurant.attribute.OrderStatuses;
import epam.eremenko.restaurant.attribute.PageAddresses;
import epam.eremenko.restaurant.attribute.ReportTypes;
import epam.eremenko.restaurant.attribute.UserRoles;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;
import epam.eremenko.restaurant.dto.ReportDto;
import epam.eremenko.restaurant.dto.UserDto;
import epam.eremenko.restaurant.entity.Menu;
import epam.eremenko.restaurant.entity.Order;
import epam.eremenko.restaurant.entity.Report;
import epam.eremenko.restaurant.entity.User;
import epam.eremenko.restaurant.service.MenuService;
import epam.eremenko.restaurant.service.OrderService;
import epam.eremenko.restaurant.service.ReportService;
import epam.eremenko.restaurant.service.UserService;
import epam.eremenko.restaurant.service.exception.ServiceException;
import epam.eremenko.restaurant.service.impl.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

class Commander {

    final Command LOCALE_CHANGER = this::changeLocale;
    final Command REGISTER = this::signUp;
    final Command SIGN_IN_INSPECTOR = this::signIn;
    final Command SIGN_OUTER = this::signOut;
    final Command ADMIN_FORMS_GETTER = this::getAdminForm;
    final Command MENU_CREATOR = this::addDish;
    final Command MENU_SUPPLIER = this::getMenu;
    final Command ORDER_COLLECTOR = this::collectOrder;
    final Command CUSTOMER_FORMS_GETTER = this::getCustomerForm;
    final Command USE_TLD_TAG = this::useTldTag;
    final Command ORDER_CREATOR = this::createOrder;
    final Command REPORTER = this::getReport;
    final Command ORDER_GETTER = this::getOrder;
    final Command FORM_GETTER = this::getAuthorizedUserForm;
    final Command ORDER_CANCELER = this::cancelOrder;
    final Command STATUS_CHANGER = this::changeOrderStatus;

    private static final UserService USER_SERVICE = ServiceFactory.getInstance().getUserService();
    private static final MenuService MENU_SERVICE = ServiceFactory.getInstance().getMenuService();
    private static final OrderService ORDER_SERVICE = ServiceFactory.getInstance().getOrderService();
    private static final ReportService REPORT_SERVICE = ServiceFactory.getInstance().getReportService();
    private static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);
    private static final int PAGINATION_RECORDS_PER_MENU_PAGE = 5;
    private static final int PAGINATION_RECORDS_PER_ORDERS_PAGE = 10;
    private static final int PAGINATION_START_PAGE = 1;
    private int paginationCurrentPage;
    private String message;


    private void changeLocale(HttpServletRequest request, HttpServletResponse response) {
        request.getSession(true).setAttribute("locale", request.getParameter("locale"));
        redirect(response, request.getHeader("referer"));
    }

    private void redirect(HttpServletResponse response, String redirectPagePath) {
        try {
            response.sendRedirect(redirectPagePath);
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void signUp(HttpServletRequest request, HttpServletResponse response) {
        UserDto userDto = DtoFactory.getUserDto(request);
        try {
            createUser(request, response, userDto);
        } catch (ServiceException ex) {
            message = ex.getMessage();
            handleException(request, response, PageAddresses.AUTHORIZATION.get());
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, String pagePath) {
        request.getSession().setAttribute("message", message);
        try {
            response.sendRedirect(pagePath);
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response, UserDto userDto)
            throws ServiceException {
        USER_SERVICE.add(userDto);
        signIn(request, response);
    }

    private void signIn(HttpServletRequest request, HttpServletResponse response) {
        UserDto userDto = DtoFactory.getUserDto(request);
        try {
            executeEntry(request, response, userDto);
        } catch (ServiceException ex) {
            message = ex.getMessage();
            handleException(request, response, PageAddresses.AUTHORIZATION.get());
        }
    }

    private void executeEntry(HttpServletRequest request, HttpServletResponse response,
                              UserDto userDto) throws ServiceException {
        User user = USER_SERVICE.get(userDto);
        setSessionAttributes(request, user);
        getOrdersIfUserIsCustomer(request);
        redirect(response, PageAddresses.MENU.get());
    }

    private void setSessionAttributes(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("role", user.getRole().get());
    }

    private void getOrdersIfUserIsCustomer(HttpServletRequest request) throws ServiceException {
        if (isUserIsCustomer(request)) {
            String reportType = ReportTypes.ACTUAL_USER_ORDERS.get();
            paginateReport(request, reportType);
        }
    }

    private void signOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        redirect(response, request.getHeader("referer"));
    }

    private void getAdminForm(HttpServletRequest request, HttpServletResponse response) {
        if (isUserDefineInTheSession(request) && isUserIsAdmin(request)) {
            getForm(request, response);
        } else {
            forwardToAuthorization(request, response);
        }
    }

    private boolean isUserDefineInTheSession(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }

    private boolean isUserIsAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return user.getRole().equals(UserRoles.ADMIN);
    }

    private void getForm(HttpServletRequest request, HttpServletResponse response) {
        String form = PageAddresses.valueOf(request.getParameter("form")).get();
        forward(request, response, form);
    }

    private void forwardToAuthorization(HttpServletRequest request, HttpServletResponse response) {
        forward(request, response, PageAddresses.AUTHORIZATION.get());
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String pagePath) {
        try {
            RequestDispatcher reqDispatcher = request.getRequestDispatcher(pagePath);
            reqDispatcher.forward(request, response);
        } catch (IOException | ServletException ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void addDish(HttpServletRequest request, HttpServletResponse response) {
        String price = request.getParameter("price").trim();
        MenuDto menuDto = DtoFactory.getMenuDto(request);
        try {
            tryAddDishToMenu(response, menuDto, price);
        } catch (ServiceException ex) {
            message = ex.getMessage();
            handleException(request, response, PageAddresses.MENU.get());
        }
    }

    private void tryAddDishToMenu(HttpServletResponse response, MenuDto menuDto,
                                  String price) throws ServiceException {
        MENU_SERVICE.add(menuDto, price);
        redirect(response, PageAddresses.MENU.get());
    }

    private void getMenu(HttpServletRequest request, HttpServletResponse response) {
        try {
            doGetMenu(request, response);
        } catch (ServiceException ex) {
            message = ex.getMessage();
            handleException(request, response, PageAddresses.MENU.get());
        }
    }

    private void doGetMenu(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException {
        String category = request.getParameter("category");
        MenuDto menuDto = DtoFactory.getMenuDto(category);
        paginateMenu(request, menuDto);
        redirect(response, PageAddresses.MENU.get());
    }

    private void paginateMenu(HttpServletRequest request,
                              MenuDto menuDto) throws ServiceException {
        definePaginationCurrentPageParameter(request);
        Menu menu = collectDishesToMenu(menuDto);
        String category = menuDto.getCategory();
        int quantityOfPages = MENU_SERVICE.getQuantityOfDishPagesIntoMenu(menu, PAGINATION_RECORDS_PER_MENU_PAGE);
        setSessionAttributes(request, category, menu, quantityOfPages);
    }

    private void definePaginationCurrentPageParameter(HttpServletRequest request) {
        resetPaginationCurrentPage();
        if (isCurrentPageHasBeenSet(request)) {
            paginationCurrentPage = Integer.parseInt(request.getParameter("page"));
        }
    }

    private void resetPaginationCurrentPage() {
        paginationCurrentPage = PAGINATION_START_PAGE;
    }

    private boolean isCurrentPageHasBeenSet(HttpServletRequest request) {
        return request.getParameter("page") != null;
    }

    private Menu collectDishesToMenu(MenuDto menuDto) throws ServiceException {
        return MENU_SERVICE.get(menuDto, paginationCurrentPage, PAGINATION_RECORDS_PER_MENU_PAGE);
    }

    private void setSessionAttributes(HttpServletRequest request, String category,
                                      Menu menu, int quantityOfPages) {
        request.getSession().setAttribute("category", category);
        request.getSession().setAttribute("menu", menu);
        request.getSession().setAttribute("quantityOfPages", quantityOfPages);
        request.getSession().setAttribute("currentPage", paginationCurrentPage);
    }

    private void collectOrder(HttpServletRequest request, HttpServletResponse response) {
        OrderDto order = getOrderDto(request);
        MenuDto dish = getOrderedDishFromRequest(request);
        ORDER_SERVICE.addDish(order, dish);
        request.getSession().setAttribute("orderDto", order);
        redirect(response, PageAddresses.MENU.get());
    }

    private OrderDto getOrderDto(HttpServletRequest request) {
        if (isOrderDtoAlreadyDefineInSession(request)) {
            User user = (User) request.getSession().getAttribute("user");
            return DtoFactory.getOrderDto(user);
        } else {
            return (OrderDto) request.getSession().getAttribute("orderDto");
        }
    }

    private boolean isOrderDtoAlreadyDefineInSession(HttpServletRequest request) {
        return request.getSession().getAttribute("orderDto") == null;
    }

    private MenuDto getOrderedDishFromRequest(HttpServletRequest request) {
        int dishId = Integer.parseInt(request.getParameter("dishId"));
        String dishName = request.getParameter("dishName");
        int dishQuantity = Integer.parseInt(request.getParameter("dishQuantity"));
        double dishPrice = Double.parseDouble(request.getParameter("dishPrice"));
        return DtoFactory.getMenuDto(dishId, dishQuantity, dishPrice, dishName);
    }

    private void getCustomerForm(HttpServletRequest request, HttpServletResponse response) {
        if (isUserDefineInTheSession(request) && isUserIsCustomer(request)) {
            getForm(request, response);
        } else {
            forwardToAuthorization(request, response);
        }
    }

    private boolean isUserIsCustomer(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return user.getRole().equals(UserRoles.CUSTOMER);
    }

    private void useTldTag(HttpServletRequest request, HttpServletResponse response) {
        String key = request.getParameter("key");
        Integer dishId = getDishFromRequest(request);
        request.setAttribute("key", key);
        request.setAttribute("dishId", dishId);
        getForm(request, response);
    }

    private Integer getDishFromRequest(HttpServletRequest request) {
        if (isDishIsDefineInTheRequest(request)) {
            return Integer.parseInt(request.getParameter("dishId"));
        }
        return null;
    }

    private boolean isDishIsDefineInTheRequest(HttpServletRequest request) {
        return request.getParameter("dishId") != null;
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response) {
        try {
            doCreateOrder(request, response);
        } catch (ServiceException ex) {
            message = "Order has not been sent:(";
            handleException(request, response, PageAddresses.MENU.get());
        }
    }

    private void doCreateOrder(HttpServletRequest request,
                               HttpServletResponse response) throws ServiceException {
        OrderDto orderDto = (OrderDto) request.getSession().getAttribute("orderDto");
        processOrder(orderDto);
        changeSessionAttribute(request);
        getReport(request, response);
    }

    private void processOrder(OrderDto orderDto) throws ServiceException {
        ORDER_SERVICE.createOrder(orderDto);
    }

    private void changeSessionAttribute(HttpServletRequest request) {
        request.getSession().setAttribute("reportType", ReportTypes.ACTUAL_USER_ORDERS.get());
        request.getSession().setAttribute("message", "The order has been successful created!");
        request.getSession().removeAttribute("orderDto");
    }

    private void getReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            checkBeforeReporting(request, response);
        } catch (ServiceException ex) {
            message = ex.getMessage();
            handleException(request, response, PageAddresses.MAIN.get());
        }
    }

    private void checkBeforeReporting(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException {
        if (isUserDefineInTheSession(request)) {
            doGetReport(request, response);
        } else {
            redirect(response, PageAddresses.AUTHORIZATION.get());
        }
    }

    private void doGetReport(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException {
        String reportType = getReportType(request);
        paginateReport(request, reportType);
        redirect(response, PageAddresses.ORDERS.get());
    }

    private String getReportType(HttpServletRequest request) {
        if (request.getParameter("reportType") != null) {
            return request.getParameter("reportType");
        } else {
            return String.valueOf(request.getSession().getAttribute("reportType"));
        }
    }

    private void paginateReport(HttpServletRequest request,
                                String reportType) throws ServiceException {
        ReportDto reportDto = getReportDtoAccordingRequestParameters(request, reportType);
        definePaginationCurrentPageParameter(request);
        Report report = getReportFromDatabase(reportDto);
        int quantityOfPages = REPORT_SERVICE.getQuantityOfPages(report, PAGINATION_RECORDS_PER_ORDERS_PAGE);
        setSessionAttributes(request, reportType, report, quantityOfPages);
    }

    private ReportDto getReportDtoAccordingRequestParameters(HttpServletRequest request, String reportType) {
        User user = (User) request.getSession().getAttribute("user");
        return DtoFactory.getReportDto(user, reportType);
    }

    private Report getReportFromDatabase(ReportDto reportDto) throws ServiceException {
        return REPORT_SERVICE.get(reportDto, paginationCurrentPage, PAGINATION_RECORDS_PER_ORDERS_PAGE);
    }

    private void setSessionAttributes(HttpServletRequest request, String reportType,
                                      Report report, int quantityOfPages) {
        request.getSession().setAttribute("reportType", reportType);
        request.getSession().setAttribute("report", report);
        request.getSession().setAttribute("quantityOfOrderPages", quantityOfPages);
        request.getSession().setAttribute("currentPage", paginationCurrentPage);
    }

    private void getOrder(HttpServletRequest request, HttpServletResponse response) {
        try {
            doGetOrder(request, response);
        } catch (ServiceException ex) {
            message = "Order has been deleted.";
            handleException(request, response, PageAddresses.ORDERS.get());
        }
    }

    private void doGetOrder(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        request.getSession().setAttribute("order", getOrderFromDatabase(request));
        getAuthorizedUserForm(request, response);
    }

    private Order getOrderFromDatabase(HttpServletRequest request) throws ServiceException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        OrderDto orderDto = DtoFactory.getOrderDto(orderId);
        return ORDER_SERVICE.get(orderDto);
    }

    private void getAuthorizedUserForm(HttpServletRequest request, HttpServletResponse response) {
        if (isUserDefineInTheSession(request)) {
            getForm(request, response);
        } else {
            forwardToAuthorization(request, response);
        }
    }

    private void cancelOrder(HttpServletRequest request, HttpServletResponse response) {
        try {
            doDelete(request, response);
        } catch (ServiceException ex) {
            message = "Operation is impossible: order is already approved";
            handleException(request, response, PageAddresses.ORDERS.get());
        }
    }

    private void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        Order order = getOrderFromDatabase(request);
        if (isOrderIsNotApprovedYet(order)) {
            ORDER_SERVICE.delete(DtoFactory.getOrderDto(order.getId()));
            getReport(request, response);
        }
    }

    private boolean isOrderIsNotApprovedYet(Order order) {
        return order != null && !order.isApproved();
    }

    private synchronized void changeOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            doChangeStatus(request, response);
        } catch (ServiceException ex) {
            message = "Status changing is impossible";
            handleException(request, response, PageAddresses.ORDER_CARD.get());
        }
    }

    private void doChangeStatus(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        Order order = (Order) request.getSession().getAttribute("order");
        OrderStatuses status = OrderStatuses.valueOf(request.getParameter("changeableStatus"));
        changeOrderStatusAfterChecking(order, status);
        getReport(request, response);
    }

    private void changeOrderStatusAfterChecking(Order order, OrderStatuses status) throws  ServiceException{
        if (status.equals(OrderStatuses.APPROVED)) {
            approveOrderIfItIsExistsAndHasNotBeenApprovedBefore(order, status);
        } else {
            approveOrder(status, order);
        }
    }

    private void approveOrderIfItIsExistsAndHasNotBeenApprovedBefore(Order order, OrderStatuses status)
            throws ServiceException {
        if (isOrderIsNotApprovedYet(order)) {
            approveOrder(status, order);
        }
    }

    private void approveOrder(OrderStatuses status, Order order) throws ServiceException {
        order.changeStatus(status);
        ORDER_SERVICE.update(order);
    }
}