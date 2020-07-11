package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.connection.ConnectionPool;
import epam.eremenko.restaurant.dao.connection.PoolFactory;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.table.ImageTable;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dao.table.UserTable;
import epam.eremenko.restaurant.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

abstract class DaoImpl<T extends Dto, E extends Enum<E>> implements Dao<T, E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoImpl.class);
    ConnectionPool connectionsPool = PoolFactory.getInstance().getCustom();

    @Override
    public void add(T t) throws DaoException {
        try {
            doAdd(t);
        } catch (SQLException | InterruptedException ex) {
            LOGGER.debug(ex.getMessage());
            throw new DaoException(ex);
        }
    }

    @Override
    public T get(T t) throws DaoException {
        try {
            return doGet(t);
        } catch (Exception ex) {
            handleException(ex);
        }
        return null;
    }

    @Override
    public void update(E e, T t) throws DaoException {
        try {
            doUpdate(e, t);
        } catch (SQLException ex) {
            LOGGER.debug(ex.getMessage());
            throw new DaoException(ex);
        }
    }

    @Override
    public void delete(T t) throws DaoException {
        int id = t.getId();
        try {
            doDelete(id, t);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    abstract void doAdd(T t) throws SQLException, InterruptedException;

    abstract T doGet(T t) throws SQLException;

    abstract void doUpdate(E e, T t) throws SQLException;


    private String getSqlQueryByObjectType(T t) {
        Map<Class<? extends Dto>, String> queries = new HashMap<>();
        queries.put(OrderDto.class, getDeleteQuery(
                OrderTable.TABLE_NAME.get(), OrderTable.ORDER_ID.get()));
        queries.put(ImageDto.class, getDeleteQuery(
                ImageTable.TABLE_NAME.get(), ImageTable.IMAGE_ID.get()));
        queries.put(MenuDto.class, getDeleteQuery(
                MenuTable.TABLE_NAME.get(), MenuTable.DISH_ID.get()));
        queries.put(UserDto.class, getDeleteQuery(
                UserTable.TABLE_NAME.get(), UserTable.USER_ID.get()));
        return queries.get(t.getClass());
    }

    private void doDelete(int id, T t) throws SQLException {
        connectionsPool.connect(getSqlQueryByObjectType(t),
                preparedStatement -> executeDeleteQuery(preparedStatement, id, t));
    }

    private String getDeleteQuery(String tableName, String columnName) {
        return "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";
    }

    private void executeDeleteQuery(PreparedStatement ps,
                                    int id, T t) throws SQLException {
        ps.setInt(1, id);
        getLogger().info(ps.executeUpdate() + t.getClass().getSimpleName() +
                " has been deleted. Id: " + id);
    }

    private void handleException(Exception ex) throws DaoException {
        LOGGER.debug(ex.toString());
        throw new DaoException(ex);
    }

    Logger getLogger() {
        return LOGGER;
    }
}
