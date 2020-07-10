package epam.eremenko.restaurant.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Command {
    void execute(HttpServletRequest request, HttpServletResponse response);
}
