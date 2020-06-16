package epam.eremenko.restaurant.dao.connection;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import epam.eremenko.restaurant.dao.Applier;

public final class ConnectionPool {
    private static final ComboPooledDataSource CPDS = new ComboPooledDataSource();
    private static final Properties PROP = new Properties();
    private static final String PROPERTIES_PATH = "src/main/resources/jdbc.properties";

    static {
        try (InputStream in = new FileInputStream(PROPERTIES_PATH)) {
            setProp(in);
        } catch (IOException | PropertyVetoException ex) {
            ex.printStackTrace();
        }
    }

    private ConnectionPool() {
    }

    private static void setProp(InputStream in) throws PropertyVetoException, IOException {
        PROP.load(in);
        CPDS.setDriverClass(PROP.getProperty("driverClass"));
        CPDS.setJdbcUrl(PROP.getProperty("jdbcUrl"));
        CPDS.setProperties(PROP);
        CPDS.setMaxStatements(Integer.parseInt(
                PROP.getProperty("maxStatements")));
        CPDS.setMaxStatementsPerConnection(Integer.parseInt(
                PROP.getProperty("maxStatementsPerConnection")));
        CPDS.setMinPoolSize(Integer.parseInt(
                PROP.getProperty("minPoolSize")));
        CPDS.setAcquireIncrement(Integer.parseInt(
                PROP.getProperty("acquireIncrement")));
        CPDS.setMaxPoolSize(Integer.parseInt(
                PROP.getProperty("maxPoolSize")));
        CPDS.setMaxIdleTime(Integer.parseInt(
                PROP.getProperty("maxIdleTime")));
    }

    public static Connection getConnection() throws SQLException {
        return CPDS.getConnection();
    }

    //TODO move to abstract class
    public static void connect(String query, Applier app) throws
            SQLException{
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            app.apply(preparedStatement);
        }
    }
}
