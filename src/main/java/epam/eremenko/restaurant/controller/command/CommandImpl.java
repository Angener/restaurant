package epam.eremenko.restaurant.controller.command;

import epam.eremenko.restaurant.controller.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserCommand {

    public final Command LOCAL_CHANGER = this::changeLocale;


    private void changeLocale(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession(true).setAttribute("local", request.getParameter("local"));
        response.sendRedirect(request.getContextPath() + "/restaurant");
    }










}
