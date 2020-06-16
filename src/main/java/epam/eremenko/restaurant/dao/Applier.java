package epam.eremenko.restaurant.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface Applier {
    void apply(PreparedStatement preparedStatement) throws SQLException;
}
