package epam.eremenko.restaurant.controller;

import epam.eremenko.restaurant.controller.command.CommandFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        clearErrors(request);
        CommandFactory.valueOf(request.getParameter("command")).get().execute(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        clearErrors(request);
        CommandFactory.valueOf(request.getParameter("command")).get().execute(request, response);
    }

    private void clearErrors(HttpServletRequest request) {
        if (isErrorExists(request.getSession())) {
            request.getSession().removeAttribute("error");
        }
    }

    private boolean isErrorExists(HttpSession session) {
        return session.getAttribute("error") != null;
    }
}
