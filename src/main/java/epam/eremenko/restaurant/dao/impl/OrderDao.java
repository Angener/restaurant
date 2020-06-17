package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.Applier;
import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.connection.ConnectionPool;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dao.table.OrderMenuTable;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class OrderDao implements Dao<OrderDto, OrderTable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDao.class);

    @Override
    public void add(OrderDto order) throws DaoException {
        try {
            addOrder(order);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void addOrder(OrderDto order) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            connection.setAutoCommit(false);
            processAddRequest(connection, order);
        }
    }

    private void processAddRequest(Connection connection, OrderDto order) throws SQLException {
        commit(connection, getAddOrderQuery(),
                preparedStatement -> executeAddOrderQuery(preparedStatement, order));
        commit(connection, getAddDishesQuery(),
                preparedStatement -> executeAddDishesQuery(preparedStatement, order));
        connection.commit();
        LOGGER.info("New order created -> order id: " + order.getOrderId()
                + ", user id:" + order.getUserId() + ".");
    }

    private void commit(Connection conn, String query, Applier app) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            app.apply(ps);
        }
    }

    private String getAddOrderQuery() {
        String fields = String.join(", ", OrderTable.TOTAL_AMOUNT.get(),
                OrderTable.USER_ID.get());
        return "INSERT INTO " + OrderTable.TABLE_NAME.get() +
                " (" + fields + ") VALUES (?,?)";
    }

    private void executeAddOrderQuery(PreparedStatement ps, OrderDto order) throws SQLException {
        sendOrder(ps, order);
        setOrderId(ps, order);
    }

    private void sendOrder(PreparedStatement ps, OrderDto order) throws SQLException {
        ps.setDouble(1, order.getTotalAmount());
        ps.setInt(2, order.getUserId());
        LOGGER.debug(ps.executeUpdate() + " order fields has been sent.");
    }

    private void setOrderId(PreparedStatement ps, OrderDto order) throws SQLException {
        int userId = order.getUserId();
        ResultSet rs = ps.executeQuery(getLastUserOrder(userId));
        rs.first();
        order.setOrderId(rs.getInt(OrderTable.ORDER_ID.get()));
        rs.close();
        LOGGER.debug("OrderId has been set :" + order.getOrderId());
    }

    private String getAddDishesQuery() {
        String fields = String.join(", ", OrderMenuTable.ORDER_ID.get(),
                OrderMenuTable.DISH_ID.get(), OrderMenuTable.QUANTITY.get(),
                OrderMenuTable.AMOUNT.get());
        return "INSERT INTO " + OrderMenuTable.TABLE_NAME.get() +
                " (" + fields + ") VALUES (?,?,?,?)";
    }

    private String getLastUserOrder(int userId) {
        return "SELECT " + OrderTable.ORDER_ID + " FROM " + OrderTable.TABLE_NAME.get() +
                " WHERE " + OrderTable.ORDER_ID + " = (SELECT MAX(" + OrderTable.ORDER_ID + ") FROM " +
                OrderTable.TABLE_NAME.get() + ") AND " + OrderTable.USER_ID + " = " + userId;
    }

    private void executeAddDishesQuery(PreparedStatement ps, OrderDto order) throws SQLException {
        List<MenuDto> dishes = order.getDishes();
        for (MenuDto dish : dishes) {
            ps.setInt(1, order.getOrderId());
            ps.setInt(2, dish.getId());
            ps.setInt(3, dish.getQuantity());
            ps.setDouble(4, dish.getAmount());
            ps.addBatch();
        }
        LOGGER.debug(Arrays.toString(ps.executeBatch()) + " rows affected. Dishes has been sent");
    }

    private void handleException(Exception ex) throws DaoException {
        LOGGER.warn(ex.toString());
        throw new DaoException(ex);
    }

    @Override
    public void update(OrderTable field, OrderDto order) throws DaoException {
        try {
            updateOrder(field, order);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void updateOrder(OrderTable field, OrderDto order) throws SQLException {
        if (isUserRequest(field)) {
            processUserRequest(order);
        } else {
            processAdminRequest(field, order);
        }
    }

    private boolean isUserRequest(OrderTable field) {
        return field.equals(OrderTable.TOTAL_AMOUNT);
    }

    private void processUserRequest(OrderDto order) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            connection.setAutoCommit(false);
            processUserOrderUpdateRequest(connection, order);
        }
    }

    private void processUserOrderUpdateRequest(Connection connection, OrderDto order) throws SQLException {
        commit(connection, getClearOrderDishesQuery(),
                preparedStatement -> executeClearDishes(preparedStatement, order));
        commit(connection, getUpdateOrderQuery(),
                preparedStatement -> executeUpdateOrderQuery(preparedStatement, order));
        commit(connection, getAddDishesQuery(),
                preparedStatement -> executeAddDishesQuery(preparedStatement, order));
        connection.commit();
        LOGGER.info("Order " + order.getOrderId() + " has been updated.");
    }

    private String getClearOrderDishesQuery() {
        return "DELETE FROM " + OrderMenuTable.TABLE_NAME.get() + " WHERE " +
                OrderMenuTable.ORDER_ID + " = ?";
    }

    private void executeClearDishes(PreparedStatement ps, OrderDto order) throws SQLException {
        ps.setInt(1, order.getOrderId());
        ps.executeUpdate();
        LOGGER.debug("Order:" + order.getOrderId() + ". Order dishes has been cleared.");
    }

    private String getUpdateOrderQuery() {
        return "UPDATE " + OrderTable.TABLE_NAME.get() +
                " SET " + OrderTable.TOTAL_AMOUNT + " = ? WHERE " +
                OrderTable.ORDER_ID + " = ? ";
    }

    private void executeUpdateOrderQuery(PreparedStatement ps, OrderDto order) throws SQLException {
        ps.setDouble(1, order.getTotalAmount());
        ps.setInt(2, order.getOrderId());
        ps.executeUpdate();
        LOGGER.debug("Order: " + order.getOrderId() + " new dishes has been sent.");
    }

    private void processAdminRequest(OrderTable field, OrderDto order) throws SQLException {
        boolean value = getUpdatableField(field, order);
        int orderId = order.getOrderId();
        updateField(field, value, orderId);
    }

    private Boolean getUpdatableField(OrderTable field, OrderDto order) {
        Map<OrderTable, Boolean> updatableFields = new EnumMap<>(OrderTable.class);
        updatableFields.put(OrderTable.IS_APPROVED, order.isApproved());
        updatableFields.put(OrderTable.IS_PASSED, order.isPassed());
        updatableFields.put(OrderTable.IS_COOKED, order.isCooked());
        updatableFields.put(OrderTable.IS_BILLED, order.isBilled());
        updatableFields.put(OrderTable.IS_PAID, order.isPaid());
        return updatableFields.get(field);
    }

    private void updateField(OrderTable field, boolean value,
                             int orderId) throws SQLException {
        ConnectionPool.connect(getAdminUpdateQuery(field),
                preparedStatement -> executeAdminUpdateQuery(preparedStatement, value, orderId));
    }

    private String getAdminUpdateQuery(OrderTable field) {
        return "UPDATE " + OrderTable.TABLE_NAME.get() + " SET " + field + " = ? WHERE " +
                OrderTable.ORDER_ID + " = ?";
    }

    private void executeAdminUpdateQuery(PreparedStatement ps,
                                         boolean value, int orderId) throws SQLException {
        ps.setBoolean(1, value);
        ps.setInt(2, orderId);
        LOGGER.info(ps.executeUpdate() + " Order id: " + orderId + " Status has been changed");
    }

    @Override
    public OrderDto get(OrderDto orderDto) throws DaoException {
        int orderId = orderDto.getOrderId();
        OrderDto order = DtoFactory.getOrderDto(orderId);
        try {
            getOrder(order);
        } catch (SQLException ex) {
            handleException(ex);
        }
        return order;
    }

    private void getOrder(OrderDto order) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection()) {
            processGetOrderRequest(connection, order);
        }
    }

    private void processGetOrderRequest(Connection connection, OrderDto order) throws SQLException {
        commit(connection, getOrderFieldsQuery(),
                preparedStatement -> collectOrderFields(preparedStatement, order));
        commit(connection, getCollectDishQuery(),
                preparedStatement -> collectDishesFromOrder(preparedStatement, order));
    }

    private String getOrderFieldsQuery() {
        String fields = String.join(", ", OrderTable.TOTAL_AMOUNT.get(),
                OrderTable.IS_APPROVED.get(), OrderTable.IS_PASSED.get(),
                OrderTable.IS_COOKED.get(), OrderTable.IS_BILLED.get(),
                OrderTable.IS_PAID.get(), OrderTable.ORDERED.get(),
                OrderTable.USER_ID.get());
        return "SELECT " + fields + " FROM " + OrderTable.TABLE_NAME.get() + " WHERE " + OrderTable.ORDER_ID +
                " = ?";
    }

    private void collectOrderFields(PreparedStatement ps, OrderDto order) throws SQLException {
        Map<OrderTable, String> param = new EnumMap<>(OrderTable.class);
        ps.setInt(1, order.getOrderId());
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        ResultSetMetaData metaData = rs.getMetaData();
        putFieldsToMap(param, metaData, rs);
        DtoFactory.configureOrderFields(order, param);
        rs.close();
    }

    private void putFieldsToMap(Map<OrderTable, String> param,
                                ResultSetMetaData metaData,
                                ResultSet rs) throws SQLException {
        int columnsQuantity = metaData.getColumnCount();
        rs.first();
        for (int i = 1; i <= columnsQuantity; i++) {
            OrderTable key = Enum.valueOf(OrderTable.class,
                    metaData.getColumnName(i).toUpperCase());
            String value = rs.getString(i);
            param.put(key, value);
        }
    }

    private String getCollectDishQuery() {
        String fields = String.join(", ", OrderMenuTable.DISH_ID.get(),
                OrderMenuTable.QUANTITY.get(), OrderMenuTable.AMOUNT.get(),
                MenuTable.DISH_NAME.get(), MenuTable.PRICE.get());
        return "SELECT " + fields + " FROM " + OrderMenuTable.TABLE_NAME.get() + " JOIN " +
                MenuTable.TABLE_NAME.get() + " USING (" + OrderMenuTable.DISH_ID + ") WHERE " +
                OrderMenuTable.ORDER_ID + " = ?";
    }

    private void collectDishesFromOrder(PreparedStatement ps, OrderDto order) throws SQLException {

        List<MenuDto> dishes = new ArrayList<>();

        ps.setInt(1, order.getOrderId());
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            addDishToOrder(rs, dishes);
        }
        rs.close();
        order.setDishes(dishes);
    }

    private void addDishToOrder(ResultSet rs, List<MenuDto> dishes) throws SQLException {
        Map<OrderMenuTable, String> orderParam = new EnumMap<>(OrderMenuTable.class);
        Map<MenuTable, String> menuParam = new EnumMap<>(MenuTable.class);
        ResultSetMetaData metaData = rs.getMetaData();
        collectFieldsFromOrderMenuTable(metaData, rs, orderParam);
        collectFieldsFromMenuTable(metaData, rs, menuParam);
        dishes.add(DtoFactory.getDishForOrderDto(orderParam, menuParam));
    }

    private void collectFieldsFromOrderMenuTable(ResultSetMetaData metaData,
                                                 ResultSet rs,
                                                 Map<OrderMenuTable, String> orderParam)
            throws SQLException {
        for (int i = 1; i < 4; i++) {
            OrderMenuTable key = Enum.valueOf(OrderMenuTable.class,
                    metaData.getColumnName(i).toUpperCase());
            String value = rs.getString(i);
            orderParam.put(key, value);
        }
    }

    private void collectFieldsFromMenuTable(ResultSetMetaData metaData,
                                            ResultSet rs,
                                            Map<MenuTable, String> menuParam)
            throws SQLException {
        for (int i = 4; i <= metaData.getColumnCount(); i++) {
            MenuTable key = Enum.valueOf(MenuTable.class,
                    metaData.getColumnName(i).toUpperCase());
            String value = rs.getString(i);
            menuParam.put(key, value);
        }
    }

    @Override
    public void delete(OrderDto order) throws DaoException {
        int orderId = order.getOrderId();
        try {
            deleteOrder(orderId);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void deleteOrder(int orderId) throws SQLException {
        ConnectionPool.connect(getDeleteQuery(),
                preparedStatement -> executeDeleteQuery(preparedStatement, orderId));
    }

    private String getDeleteQuery() {
        return "DELETE FROM " + OrderTable.TABLE_NAME.get() + " WHERE " +
                OrderTable.ORDER_ID + " = ?";
    }

    private void executeDeleteQuery(PreparedStatement ps,
                                    int orderId) throws SQLException {
        ps.setInt(1, orderId);
        LOGGER.info(ps.executeUpdate() + " order has been deleted. OrderId: " + orderId);
    }
}
