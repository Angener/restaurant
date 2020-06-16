package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.*;
import epam.eremenko.restaurant.dao.connection.ConnectionPool;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.table.UserTable;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDao implements Dao<UserDto, UserTable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

    @Override
    public void add(UserDto user) throws DaoException {
        try {
            addUserToDatabase(user);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void addUserToDatabase(UserDto user) throws SQLException {
        ConnectionPool.connect(getAddQuery(),
                preparedStatement -> executeAddQuery(preparedStatement, user));
    }

    private String getAddQuery() {
        String fields = String.join(", ", UserTable.USERNAME.get(),
                UserTable.EMAIL.get(), UserTable.PASSWORD.get(),
                UserTable.MOBILE.get());
        return "INSERT INTO " + UserTable.TABLE_NAME.get() +
                " (" + fields + ") VALUES (?,?,?,?)";
    }

    private void executeAddQuery(PreparedStatement ps, UserDto user) throws SQLException {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getMobile());
        LOGGER.debug(ps.executeUpdate() + " has been added. Username: " + user.getUsername());
    }

    private void handleException(Exception ex) throws DaoException {
        LOGGER.warn(ex.toString());
        throw new DaoException(ex);
    }

    @Override
    public void update(UserTable uf, UserDto user) throws DaoException {
        String value = getUpdatableField(uf, user);
        int userId = user.getUserId();
        try {
            updateField(uf, value, userId);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private String getUpdatableField(UserTable uf, UserDto user) {
        Map<UserTable, String> newValues = new HashMap<>();
        newValues.put(UserTable.USERNAME, user.getUsername());
        newValues.put(UserTable.EMAIL, user.getEmail());
        newValues.put(UserTable.PASSWORD, user.getPassword());
        newValues.put(UserTable.MOBILE, user.getMobile());
        newValues.put(UserTable.ROLE, user.getRole().toString());
        return newValues.get(uf);
    }

    private void updateField(UserTable uf, String value,
                             int userId) throws SQLException {
        ConnectionPool.connect(getUpdateQuery(uf),
                preparedStatement -> executeUpdateQuery(preparedStatement, value, userId));
    }

    private String getUpdateQuery(UserTable uf) {
        return "UPDATE " + UserTable.TABLE_NAME.get() + " SET "
                + uf.toString() + " = ? WHERE " + UserTable.USER_ID + " = ?";
    }

    private void executeUpdateQuery(PreparedStatement ps,
                                    String value, int userId) throws SQLException {
        ps.setString(1, value);
        ps.setInt(2, userId);
        LOGGER.debug(ps.executeUpdate() + " user updated. User id: " + userId);
    }

    @Override
    public UserDto get(UserDto user) throws DaoException {
        Map<UserTable, String> param = new HashMap<>();
        String username = user.getUsername();
        try {
            getFields(username, param);
        } catch (SQLException ex) {
            handleException(ex);
        }
        return DtoFactory.getUserDto(param);
    }

    private void getFields(String username, Map<UserTable, String> param)
            throws SQLException {
        ConnectionPool.connect(getGetQuery(),
                preparedStatement -> executeGetQuery(preparedStatement, username, param));
    }

    private String getGetQuery() {
        return "SELECT * FROM " + UserTable.TABLE_NAME.get() + " WHERE " +
                UserTable.USERNAME + " = ?";
    }

    private void executeGetQuery(PreparedStatement ps, String username,
                                 Map<UserTable, String> param) throws SQLException {
        ps.setString(1, username);
        ps.executeQuery();
        collectFieldsFromResultSet(ps, param);
    }

    private void collectFieldsFromResultSet(PreparedStatement ps,
                                            Map<UserTable, String> param) throws SQLException {
        try (ResultSet resultSet = ps.getResultSet()) {
            scanTableColumns(resultSet, param);
        }
    }

    private void scanTableColumns(ResultSet resultSet,
                                  Map<UserTable, String> param) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnsQuantity = metaData.getColumnCount();
        resultSet.first();
        for (int i = 1; i <= columnsQuantity; i++) {
            UserTable key = Enum.valueOf(UserTable.class,
                    metaData.getColumnName(i).toUpperCase());
            String value = resultSet.getString(i);
            param.put(key, value);
        }
    }

    @Override
    public void delete(UserDto user) throws DaoException {
        int userId = user.getUserId();
        try {
            deleteUser(userId);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM " + UserTable.TABLE_NAME.get() + " WHERE " +
                UserTable.USER_ID + " = ?";
        ConnectionPool.connect(query,
                preparedStatement -> executeDeleteQuery(preparedStatement, userId));
    }

    private void executeDeleteQuery(PreparedStatement ps, int userId) throws SQLException {
        ps.setInt(1, userId);
        LOGGER.debug(ps.executeUpdate() + " user deleted. User id" + userId);
    }
}
