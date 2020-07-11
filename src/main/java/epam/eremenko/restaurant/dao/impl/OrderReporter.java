package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.exception.DaoException;
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

public class OrderReporter extends DaoImpl<ReportDto, OrderTable> implements Dao<ReportDto, OrderTable> {

    @Override
    public ReportDto doGet(ReportDto report) throws SQLException {
        List<OrderDto> orders = new ArrayList<>();
        int userId = report.getUserId();
        getOrders(userId, orders);
        return DtoFactory.getReportDto(orders);
    }

    private void getOrders(int userId, List<OrderDto> orders)
            throws SQLException {
        connectionsPool.connect(getSqlQueryRetrievingOrderReportByUserOrAllCompanyOrders(userId),
                preparedStatement -> executeGetQuery(preparedStatement, orders));
    }

    private String getSqlQueryRetrievingOrderReportByUserOrAllCompanyOrders(int userId) {
        if (userId == 0) {
            return getSqlQueryGettingAllCompanyOrders();
        } else {
            return getSqlQueryGettingAllUserOrders(userId);
        }
    }

    private String getSqlQueryGettingAllCompanyOrders() {
        return "SELECT * FROM " + OrderTable.TABLE_NAME.get();
    }

    private String getSqlQueryGettingAllUserOrders(int userId) {
        return "SELECT * FROM " + OrderTable.TABLE_NAME.get() +
                " WHERE " + OrderTable.USER_ID + " = '" + userId + "'";
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
    void doAdd(ReportDto reportDto) throws SQLException, InterruptedException {
    }

    @Override
    void doUpdate(OrderTable orderTable, ReportDto reportDto) throws SQLException {
    }

    @Override
    public void delete(ReportDto dto) throws DaoException {
    }
}
