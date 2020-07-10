package epam.eremenko.restaurant.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {
    Logger LOGGER = LoggerFactory.getLogger(HelloServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setCharacterEncoding("Cp1251");
//
//        List<String> list = new Dao().getOrder();
//
//        for (String i : list){
//            response.getWriter().println(i);
//            LOGGER.info(""+i);
//        }
    }
}
