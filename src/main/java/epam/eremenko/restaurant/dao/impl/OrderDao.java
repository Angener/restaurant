package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.Applier;
import epam.eremenko.restaurant.dao.connection.impl.c3po.C3p0;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dao.table.OrderMenuTable;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;

import java.sql.*;
import java.util.*;

public class OrderDao extends DaoImpl<OrderDto, OrderTable> {

    @Override
    void doAdd(OrderDto order) throws SQLException {
        addOrder(order);
    }

    private void addOrder(OrderDto order) throws SQLException {
        try (Connection connection = connectionsPool.getConnection()) {
            connection.setAutoCommit(false);
            processAddRequest(connection, order);
            connection.commit();
            getLogger().info("New order created -> order id: " + order.getId()
                    + ", user id:" + order.getUserId() + ".");
        }
    }

    private void processAddRequest(Connection connection, OrderDto order) throws SQLException {
        commit(connection, getSqlQueryAddingNewOrderToOrderTable(),
                preparedStatement -> executeSqlQueryAddingNewOrderToOrderTable(preparedStatement, order));
        commit(connection, getSqlQueryAddingDishesToOrder(),
                preparedStatement -> executeSqlQueryAddingDishesToOrder(preparedStatement, order));
    }

    private void commit(Connection conn, String query, Applier app) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            app.apply(ps);
        }
    }

    private String getSqlQueryAddingNewOrderToOrderTable() {
        String fields = String.join(", ", OrderTable.TOTAL_AMOUNT.get(),
                OrderTable.USER_ID.get());
        return "INSERT INTO " + OrderTable.TABLE_NAME.get() +
                " (" + fields + ") VALUES (?,?)";
    }

    private void executeSqlQueryAddingNewOrderToOrderTable(PreparedStatement ps, OrderDto order)
            throws SQLException {
        fillDataToOrderTable(ps, order);
        setIdFromCreatedOrder(ps, order);
    }

    private void fillDataToOrderTable(PreparedStatement ps, OrderDto order) throws SQLException {
        ps.setDouble(1, order.getTotalAmount());
        ps.setInt(2, order.getUserId());
        getLogger().debug(ps.executeUpdate() + " order fields has been sent.");
    }

    private void setIdFromCreatedOrder(PreparedStatement ps, OrderDto order) throws SQLException {
        int userId = order.getUserId();
        ResultSet rs = ps.executeQuery(getSqlQueryCheckingLastUsersOrder(userId));
        rs.first();
        order.setId(rs.getInt(OrderTable.ORDER_ID.get()));
        rs.close();
        getLogger().debug("OrderId has been set :" + order.getId());
    }

    private String getSqlQueryAddingDishesToOrder() {
        String fields = String.join(", ", OrderMenuTable.ORDER_ID.get(),
                OrderMenuTable.DISH_ID.get(), OrderMenuTable.QUANTITY.get(),
                OrderMenuTable.AMOUNT.get());
        return "INSERT INTO " + OrderMenuTable.TABLE_NAME.get() +
                " (" + fields + ") VALUES (?,?,?,?)";
    }

    private String getSqlQueryCheckingLastUsersOrder(int userId) {
        return "SELECT " + OrderTable.ORDER_ID + " FROM " + OrderTable.TABLE_NAME.get() +
                " WHERE " + OrderTable.ORDER_ID + " = (SELECT MAX(" + OrderTable.ORDER_ID + ") FROM " +
                OrderTable.TABLE_NAME.get() + ") AND " + OrderTable.USER_ID + " = " + userId;
    }

    private void executeSqlQueryAddingDishesToOrder(PreparedStatement ps, OrderDto order)
            throws SQLException {
        List<MenuDto> dishes = order.getDishes();
        for (MenuDto dish : dishes) {
            ps.setInt(1, order.getId());
            ps.setInt(2, dish.getId());
            ps.setInt(3, dish.getQuantity());
            ps.setDouble(4, dish.getAmount());
            ps.addBatch();
        }
        getLogger().debug(Arrays.toString(ps.executeBatch()) + " rows affected. Dishes has been sent");
    }

    @Override
    OrderDto doGet(OrderDto order) throws SQLException {
        getOrder(order);
        return order;
    }

    private void getOrder(OrderDto order) throws SQLException {
        try (Connection connection = connectionsPool.getConnection()) {
            connection.setAutoCommit(false);
            processGetOrderRequest(connection, order);
            connection.commit();
        }
    }

    private void processGetOrderRequest(Connection connection, OrderDto order) throws SQLException {
        commit(connection, getSqlQueryCollectingOrderMetadata(),
                preparedStatement -> collectOrderMetadata(preparedStatement, order));
        commit(connection, getSqlQueryCollectingDishesFromOrder(),
                preparedStatement -> collectDishesFromOrder(preparedStatement, order));
    }

    private String getSqlQueryCollectingOrderMetadata() {
        String fields = String.join(", ", OrderTable.TOTAL_AMOUNT.get(),
                OrderTable.IS_APPROVED.get(), OrderTable.IS_PASSED.get(),
                OrderTable.IS_COOKED.get(), OrderTable.IS_BILLED.get(),
                OrderTable.IS_PAID.get(), OrderTable.ORDERED.get(),
                OrderTable.USER_ID.get());
        return "SELECT " + fields + " FROM " + OrderTable.TABLE_NAME.get() +
                " WHERE " + OrderTable.ORDER_ID + " = ?";
    }

    private void collectOrderMetadata(PreparedStatement ps, OrderDto order) throws SQLException {
        Map<OrderTable, String> param = new EnumMap<>(OrderTable.class);
        ps.setInt(1, order.getId());
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        ResultSetMetaData metaData = rs.getMetaData();
        putFieldToMap(param, metaData, rs);
        DtoFactory.configureOrderFields(order, param);
        rs.close();
    }

    private void putFieldToMap(Map<OrderTable, String> param,
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

    private String getSqlQueryCollectingDishesFromOrder() {
        String fields = String.join(", ", OrderMenuTable.DISH_ID.get(),
                OrderMenuTable.QUANTITY.get(), OrderMenuTable.AMOUNT.get(),
                MenuTable.DISH_NAME.get(), MenuTable.PRICE.get());
        return "SELECT " + fields + " FROM " + OrderMenuTable.TABLE_NAME.get() + " JOIN " +
                MenuTable.TABLE_NAME.get() + " USING (" + OrderMenuTable.DISH_ID + ") WHERE " +
                OrderMenuTable.ORDER_ID + " = ?";
    }

    private void collectDishesFromOrder(PreparedStatement ps, OrderDto order) throws SQLException {
        List<MenuDto> dishes = new ArrayList<>();
        ps.setInt(1, order.getId());
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
    public void doUpdate(OrderTable field, OrderDto order) throws SQLException {
        defineRequestTypeOfUserOrAdmin(field, order);
    }

    private void defineRequestTypeOfUserOrAdmin(OrderTable field, OrderDto order) throws SQLException {
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
        try (Connection connection = connectionsPool.getConnection()) {
            connection.setAutoCommit(false);
            executeUserOrderUpdateRequest(connection, order);
        }
    }

    private void executeUserOrderUpdateRequest(Connection connection, OrderDto order) throws SQLException {
        commit(connection, getSqlQueryCleaningAllOrderDishes(),
                preparedStatement -> prepareQueryCleaningAllOrderDishes(preparedStatement, order));
        commit(connection, getSqlQueryUpdatingOrder(),
                preparedStatement -> prepareSqlQueryUpdatingOrder(preparedStatement, order));
        commit(connection, getSqlQueryAddingDishesToOrder(),
                preparedStatement -> executeSqlQueryAddingDishesToOrder(preparedStatement, order));
        connection.commit();
        getLogger().info("Order " + order.getId() + " has been updated.");
    }

    private String getSqlQueryCleaningAllOrderDishes() {
        return "DELETE FROM " + OrderMenuTable.TABLE_NAME.get() + " WHERE " + OrderMenuTable.ORDER_ID + " = ?";
    }

    private void prepareQueryCleaningAllOrderDishes(PreparedStatement ps, OrderDto order) throws SQLException {
        ps.setInt(1, order.getId());
        ps.executeUpdate();
        getLogger().debug("Order:" + order.getId() + ". Order dishes has been cleared.");
    }

    private String getSqlQueryUpdatingOrder() {
        return "UPDATE " + OrderTable.TABLE_NAME.get() + " SET " + OrderTable.TOTAL_AMOUNT + " = ? WHERE " +
                OrderTable.ORDER_ID + " = ? ";
    }

    private void prepareSqlQueryUpdatingOrder(PreparedStatement ps, OrderDto order) throws SQLException {
        ps.setDouble(1, order.getTotalAmount());
        ps.setInt(2, order.getId());
        ps.executeUpdate();
        getLogger().debug("Order: " + order.getId() + " new dishes has been sent.");
    }

    private void processAdminRequest(OrderTable field, OrderDto order) throws SQLException {
        boolean value = getUpdatableField(field, order);
        int orderId = order.getId();
        executeAdminUpdateRequest(field, value, orderId);
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

    private void executeAdminUpdateRequest(OrderTable field, boolean value,
                                           int orderId) throws SQLException {
        connectionsPool.connect(getSqlAdminQueryUpdatingOrderField(field),
                preparedStatement -> prepareAdminUpdateQuery(preparedStatement, value, orderId));
    }

    private String getSqlAdminQueryUpdatingOrderField(OrderTable field) {
        return "UPDATE " + OrderTable.TABLE_NAME.get() + " SET " + field + " = ? WHERE " +
                OrderTable.ORDER_ID + " = ?";
    }

    private void prepareAdminUpdateQuery(PreparedStatement ps,
                                         boolean value, int orderId) throws SQLException {
        ps.setBoolean(1, value);
        ps.setInt(2, orderId);
        getLogger().info(ps.executeUpdate() + " Order id: " + orderId + " Status has been changed");
    }
}
