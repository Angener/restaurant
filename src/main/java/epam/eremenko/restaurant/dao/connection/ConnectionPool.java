package epam.eremenko.restaurant.dao.connection;

import epam.eremenko.restaurant.dao.Applier;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
    Connection getConnection() throws SQLException;

    void connect(String query, Applier app) throws SQLException;
}
