package epam.eremenko.restaurant.controller.command;


import epam.eremenko.restaurant.config.PageAddresses;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.UserDto;
import epam.eremenko.restaurant.entity.BeanFactory;
import epam.eremenko.restaurant.entity.Menu;
import epam.eremenko.restaurant.entity.User;
import epam.eremenko.restaurant.service.MenuService;
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

class CommandImpl {

    final Command LOCALE_CHANGER = this::changeLocale;
    final Command REGISTER = this::signUp;
    final Command SIGN_IN_INSPECTOR = this::signIn;
    final Command SIGN_OUTER = this::signOut;
    final Command MENU_FORMATTER = this::getAddMenuForm;
    final Command MENU_CREATOR = this::addDish;
    final Command MENU_SUPPLIER = this::getMenu;

    private final static UserService USER_SERVICE = ServiceFactory.getInstance().getUserService();
    private final static MenuService MENU_SERVICE = ServiceFactory.getInstance().getMenuService();
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandImpl.class);
    private static final int PAGINATION_RECORDS_PER_PAGE = 5;
    private static final int PAGINATION_START_PAGE = 1;
    private int paginationCurrentPage;
    private String errorMessage;


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
            errorMessage = ex.getMessage();
            handleException(request, response, PageAddresses.AUTHORIZATION.get());
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
            errorMessage = ex.getMessage();
            handleException(request, response, PageAddresses.AUTHORIZATION.get());
        }
    }

    private void executeEntry(HttpServletRequest request, HttpServletResponse response,
                              UserDto userDto) throws ServiceException {
        User user = USER_SERVICE.get(userDto);
        setSessionAttributes(request, user);
        redirect(response, PageAddresses.MENU.get());
    }

    private void setSessionAttributes(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("role", user.getRole().get());
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, String pagePath) {
        request.getSession().setAttribute("error", errorMessage);
        try {
            response.sendRedirect(pagePath);
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void signOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        redirect(response, request.getHeader("referer"));
    }

    private void getAddMenuForm(HttpServletRequest request, HttpServletResponse response) {
        forward(request, response, PageAddresses.MENU_REGISTER.get());
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
            errorMessage = ex.getMessage();
            handleException(request, response, PageAddresses.MENU_REGISTER.get());
        }
    }

    private void tryAddDishToMenu(HttpServletResponse response, MenuDto menuDto,
                                  String price) throws ServiceException {
        MENU_SERVICE.add(menuDto, price);
        redirect(response, PageAddresses.MENU.get());
    }

    private void getMenu(HttpServletRequest request, HttpServletResponse response) {
        String category = request.getParameter("category");
        MenuDto menuDto = DtoFactory.getMenuDto(category);
        definePaginationCurrentPageParameter(request);
        Menu menu = collectDishesToMenu(request, response, menuDto);
        int quantityOfPages = MENU_SERVICE.getQuantityOfDishPagesIntoMenu(menu, PAGINATION_RECORDS_PER_PAGE);
        setSessionAttributes(request, category, menu, quantityOfPages);
        redirect(response, PageAddresses.MENU.get());
    }

    private void definePaginationCurrentPageParameter(HttpServletRequest request) {
        resetPaginationCurrentPage();
        if (isCurrentPageHasBeenSet(request)) {
            paginationCurrentPage = Integer.parseInt(request.getParameter("page"));
        }
    }

    private void resetPaginationCurrentPage(){
        paginationCurrentPage = PAGINATION_START_PAGE;
    }

    private boolean isCurrentPageHasBeenSet(HttpServletRequest request) {
        return request.getParameter("page") != null;
    }

    private Menu collectDishesToMenu(HttpServletRequest request, HttpServletResponse response, MenuDto menuDto) {
        Menu menu = BeanFactory.getInstance().getMenu();
        try {
            return MENU_SERVICE.get(menuDto, paginationCurrentPage, PAGINATION_RECORDS_PER_PAGE);
        } catch (ServiceException ex) {
            errorMessage = ex.getMessage();
            handleException(request, response, PageAddresses.MENU.get());
        }
        return menu;
    }

    private void setSessionAttributes(HttpServletRequest request, String category, Menu menu, int quantityOfPages) {
        HttpSession session = request.getSession();
        session.setAttribute("category", category);
        session.setAttribute("menu", menu);
        session.setAttribute("quantityOfPages", quantityOfPages);
        session.setAttribute("currentPage", paginationCurrentPage);
    }
}
