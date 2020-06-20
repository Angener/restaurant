package epam.eremenko.restaurant.dao.connection.impl.c3po;

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
import epam.eremenko.restaurant.dao.connection.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class C3p0 implements ConnectionPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(C3p0.class);
    private static final ComboPooledDataSource CPDS = new ComboPooledDataSource();
    private static final Properties PROP = new Properties();
    private static final String PROPERTIES_PATH = "src/main/resources/jdbc.properties";


    public C3p0() {
        try (InputStream in = new FileInputStream(PROPERTIES_PATH)) {
            setProp(in);
        } catch (IOException | PropertyVetoException ex) {
            LOGGER.warn(ex.toString());
        }
    }

    private void setProp(InputStream in) throws PropertyVetoException, IOException {
        PROP.load(in);
        CPDS.setDriverClass(PROP.getProperty("driverClass"));
        CPDS.setJdbcUrl(PROP.getProperty("url"));
        CPDS.setProperties(PROP);
        CPDS.setMaxStatements(Integer.parseInt(PROP.getProperty("maxStatements")));
        CPDS.setMaxStatementsPerConnection(Integer.parseInt(PROP.getProperty("maxStatementsPerConnection")));
        CPDS.setMinPoolSize(Integer.parseInt(PROP.getProperty("minPoolSize")));
        CPDS.setInitialPoolSize(Integer.parseInt(PROP.getProperty("initialPoolSize")));
        CPDS.setAcquireIncrement(Integer.parseInt(PROP.getProperty("acquireIncrement")));
        CPDS.setMaxPoolSize(Integer.parseInt(PROP.getProperty("maxPoolSize")));
        CPDS.setMaxIdleTime(Integer.parseInt(PROP.getProperty("maxIdleTime")));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return CPDS.getConnection();
    }

    @Override
    public void connect(String query, Applier app) throws
            SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            app.apply(preparedStatement);
        }
    }
}
