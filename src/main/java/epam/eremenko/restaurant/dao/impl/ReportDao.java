package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.OrderDto;
import epam.eremenko.restaurant.dto.ReportDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ReportDao extends DaoImpl<ReportDto, OrderTable> implements Dao<ReportDto, OrderTable> {

    @Override
    public ReportDto doGet(ReportDto reportDto) throws SQLException {
        List<OrderDto> orders = new ArrayList<>();
        getOrders(reportDto, orders);
        return DtoFactory.getReportDto(orders);
    }

    private void getOrders(ReportDto reportDto, List<OrderDto> orders)
            throws SQLException {
        connectionsPool.connect(defineSqlQuery(reportDto),
                preparedStatement -> executeGetQuery(preparedStatement, orders));
    }

    private String defineSqlQuery(ReportDto reportDto) {
        return switch (reportDto.getType()) {
            case ACTUAL_USER_ORDERS -> getSqlQueryGettingActualUserOrders(reportDto);
            case INCOMPLETE_ORDERS -> getSqlQueryGettingReportByParameter(OrderTable.IS_PAID, 0);
            case UNAPPROVED_ORDERS -> getSqlQueryGettingReportByParameter(OrderTable.IS_APPROVED, 0);
            case UNSENT_TO_KITCHEN_ORDERS -> getSqlQueryGettingReportByParameter(OrderTable.IS_APPROVED, OrderTable.IS_PASSED);
            case UNCOOKED_ORDERS -> getSqlQueryGettingReportByParameter(OrderTable.IS_PASSED, OrderTable.IS_COOKED);
            case NOT_BILLED_ORDERS -> getSqlQueryGettingReportByParameter(OrderTable.IS_COOKED, OrderTable.IS_BILLED);
            case COMPLETED_ORDERS -> getSqlQueryGettingReportByParameter(OrderTable.IS_PAID, 1);
            case ALL_ORDERS -> getSqlQueryGettingAllOrders();
        };
    }

    private String getSqlQueryGettingActualUserOrders(ReportDto reportDto) {
        int userId = reportDto.getUserId();
        return "SELECT * FROM " + OrderTable.TABLE_NAME.get() +
                " WHERE " + OrderTable.USER_ID + " = '" + userId + "'" +
                " AND " + OrderTable.IS_PAID + " = 0";
    }

    private String getSqlQueryGettingReportByParameter(OrderTable parameter, OrderTable nextParameter) {
        return "SELECT * FROM " + OrderTable.TABLE_NAME.get() +
                " WHERE " + parameter + " = '1' AND " + nextParameter + " = '0'";
    }


    private String getSqlQueryGettingReportByParameter(OrderTable parameter, int value) {
        return "SELECT * FROM " + OrderTable.TABLE_NAME.get() +
                " WHERE " + parameter + " = '" + value + "'";
    }

    private String getSqlQueryGettingAllOrders() {
        return "SELECT * FROM " + OrderTable.TABLE_NAME.get();
    }

    private void executeGetQuery(PreparedStatement ps,
                                 List<OrderDto> orders) throws SQLException {
        ps.executeQuery();
        try (ResultSet rs = ps.getResultSet()) {
            collectOrdersDto(rs, orders);
        }
    }

    private void collectOrdersDto(ResultSet rs, List<OrderDto> orders) throws SQLException {
        while (rs.next()) {
            orders.add(getOrder(rs));
        }
    }

    private OrderDto getOrder(ResultSet rs) throws SQLException {
        return DtoFactory.getOrderDto(scanOrderFields(rs));
    }

    private Map<OrderTable, String> scanOrderFields(ResultSet rs) throws SQLException {
        Map<OrderTable, String> param = new EnumMap<>(OrderTable.class);
        ResultSetMetaData metaData = rs.getMetaData();
        putFieldsToMap(param, metaData, rs);
        return param;
    }

    private void putFieldsToMap(Map<OrderTable, String> param, ResultSetMetaData metaData,
                                ResultSet rs) throws SQLException {
        int columnsQuantity = metaData.getColumnCount();
        for (int i = 1; i <= columnsQuantity; i++) {
            OrderTable key = Enum.valueOf(OrderTable.class,
                    metaData.getColumnName(i).toUpperCase());
            String value = rs.getString(i);
            param.put(key, value);
        }
    }

    @Override
    void doAdd(ReportDto reportDto) {
    }

    @Override
    void doUpdate(OrderTable orderTable, ReportDto reportDto) {
    }

    @Override
    public void delete(ReportDto dto) {
    }
}
