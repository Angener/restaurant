package epam.eremenko.restaurant.filter;

import epam.eremenko.restaurant.config.PageAddresses;
import epam.eremenko.restaurant.config.UserRoles;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class EntryFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!isAdmin(getUserRole(request))) {
            forwardToRegister(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isAdmin(String role) {
        return (UserRoles.ADMIN.get().equals(role));
    }

    private String getUserRole(ServletRequest request) {
        HttpSession session = ((HttpServletRequest) request).getSession();
        return (String) session.getAttribute("role");
    }

    private void forwardToRegister(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(((HttpServletRequest) request).getContextPath()
                + PageAddresses.AUTHORIZATION.get()).forward(request, response);
    }

    @Override
    public void init(FilterConfig fConfig) {
    }

    @Override
    public void destroy() {
    }
}
