package epam.eremenko.restaurant.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Encoder implements Filter {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String CHARACTER_ENCODING = "UTF-8";
    private String encoding;

    @Override
    public void init(FilterConfig config) {
        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null) encoding = "UTF-8";
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        setProperties(request, response);
        next.doFilter(request, response);
    }

    private void setProperties(ServletRequest request, ServletResponse response)
            throws UnsupportedEncodingException {
        if (isSetEncoding(request)) {
            request.setCharacterEncoding(encoding);
        }
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
    }

    private boolean isSetEncoding(ServletRequest request){
        return null == request.getCharacterEncoding();
    }
}
