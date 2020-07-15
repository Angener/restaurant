package epam.eremenko.restaurant.filter;

import epam.eremenko.restaurant.attribute.PageAddresses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Blocker implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Blocker.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isUserUnauthorized(request)) {
            forwardUserToRegisterPage(request, response);
        }
        chain.doFilter(request, response);
    }


    private boolean isUserUnauthorized(ServletRequest request) {
        HttpSession session = ((HttpServletRequest) request).getSession();
        return session.getAttribute("user") == null;
    }

    private void forwardUserToRegisterPage(ServletRequest request, ServletResponse response) {
        try {
            request.getRequestDispatcher(((HttpServletRequest) request).getContextPath()
                    + PageAddresses.AUTHORIZATION.get()).forward(request, response);
        } catch (ServletException | IOException ex) {
            LOGGER.error(ex.toString());
        }
    }

    @Override
    public void init(FilterConfig fConfig) {
    }

    @Override
    public void destroy() {
    }
}