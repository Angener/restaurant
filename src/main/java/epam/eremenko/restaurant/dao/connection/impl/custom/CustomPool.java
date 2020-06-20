package epam.eremenko.restaurant.dao.connection.impl.custom;

import epam.eremenko.restaurant.dao.Applier;
import epam.eremenko.restaurant.dao.connection.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public final class CustomPool implements ConnectionPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPool.class);
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_PATH = "src/main/resources/jdbc.properties";
    private static final CustomConnectionManager MANAGER = CustomConnectionManager.getInstance();

    public CustomPool() {
        try (InputStream in = new FileInputStream(PROPERTIES_PATH)) {
            setProp(in);
        } catch (IOException | SQLException ex) {
            LOGGER.warn(ex.toString());
        }
    }

    private void setProp(InputStream in) throws IOException, SQLException {
        PROPERTIES.load(in);
        MANAGER.setUrl(PROPERTIES.getProperty("url"));
        MANAGER.setUser(PROPERTIES.getProperty("user"));
        MANAGER.setPassword(PROPERTIES.getProperty("password"));
        MANAGER.setUseUnicode(PROPERTIES.getProperty("useUnicode"));
        MANAGER.setCharacterEncoding(PROPERTIES.getProperty("characterEncoding"));
        MANAGER.setInitialPoolSize(Integer.parseInt(PROPERTIES.getProperty("initialPoolSize")));
        MANAGER.setMinPoolSize(Integer.parseInt(PROPERTIES.getProperty("minPoolSize")));
        MANAGER.setMaxPoolSize(Integer.parseInt(PROPERTIES.getProperty("maxPoolSize")));
        MANAGER.setConnectThresholdPerConnection(Integer.parseInt(PROPERTIES.getProperty("threshold")));
        MANAGER.setAcquireIncrement(Integer.parseInt(PROPERTIES.getProperty("acquireIncrement")));
        MANAGER.init();
    }

    @Override
    public Connection getConnection() {
        return MANAGER.getConnection();
    }

    @Override
    public void connect(String query, Applier app) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            app.apply(preparedStatement);
        }
    }
}





