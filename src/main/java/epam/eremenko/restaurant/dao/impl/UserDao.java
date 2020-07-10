package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.table.UserTable;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.UserDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

class UserDao extends DaoImpl<UserDto, UserTable> {

    @Override
    public void doAdd(UserDto user) throws SQLException {
        addUserToDatabase(user);
    }

    private void addUserToDatabase(UserDto user) throws SQLException {
        connectionsPool.connect(getSqlAddingQuery(),
                preparedStatement -> executeAddQuery(preparedStatement, user));
    }

    private String getSqlAddingQuery() {
        String fields = String.join(", ", UserTable.USERNAME.get(),
                UserTable.EMAIL.get(), UserTable.PASSWORD.get(),
                UserTable.MOBILE.get());
        return "INSERT INTO " + UserTable.TABLE_NAME.get() + " (" + fields + ") VALUES (?,?,?,?)";
    }

    private void executeAddQuery(PreparedStatement ps, UserDto user) throws SQLException {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getMobile());
        getLogger().debug(ps.executeUpdate() + " has been added. Username: " + user.getUsername());
    }

    @Override
    public UserDto doGet(UserDto user) throws SQLException {
        Map<UserTable, String> param = new EnumMap<>(UserTable.class);
        String username = user.getUsername();
        getUser(username, param);
        return DtoFactory.getUserDto(param);
    }

    private void getUser(String username, Map<UserTable, String> param) throws SQLException {
        connectionsPool.connect(getSqlQueryGettingUser(),
                preparedStatement -> executeGetQuery(preparedStatement, username, param));
    }

    private String getSqlQueryGettingUser() {
        return "SELECT * FROM " + UserTable.TABLE_NAME.get() + " WHERE " + UserTable.USERNAME + " = ?";
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
            putColumnNamesOfTableToMap(resultSet, param);
        }
    }

    private void putColumnNamesOfTableToMap(ResultSet resultSet,
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
    public void doUpdate(UserTable uf, UserDto user) throws SQLException {
        String value = getUpdatableField(uf, user);
        int userId = user.getId();
        updateField(uf, value, userId);
    }

    private String getUpdatableField(UserTable uf, UserDto user) {
        Map<UserTable, String> newValues = new EnumMap<>(UserTable.class);
        newValues.put(UserTable.USERNAME, user.getUsername());
        newValues.put(UserTable.EMAIL, user.getEmail());
        newValues.put(UserTable.PASSWORD, user.getPassword());
        newValues.put(UserTable.MOBILE, user.getMobile());
        newValues.put(UserTable.ROLE, user.getRole().toString());
        return newValues.get(uf);
    }

    private void updateField(UserTable uf, String value, int userId) throws SQLException {
        connectionsPool.connect(getSqlQueryUpdatingFields(uf),
                preparedStatement -> executeUpdateQuery(preparedStatement, value, userId));
    }

    private String getSqlQueryUpdatingFields(UserTable uf) {
        return "UPDATE " + UserTable.TABLE_NAME.get() + " SET "
                + uf.toString() + " = ? WHERE " + UserTable.USER_ID + " = ?";
    }

    private void executeUpdateQuery(PreparedStatement ps,
                                    String value, int userId) throws SQLException {
        ps.setString(1, value);
        ps.setInt(2, userId);
        getLogger().debug(ps.executeUpdate() + " user updated. User id: " + userId);
    }
}
