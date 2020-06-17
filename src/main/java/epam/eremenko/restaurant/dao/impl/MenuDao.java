package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.table.ImageTable;
import epam.eremenko.restaurant.config.MenuCategories;
import epam.eremenko.restaurant.dao.connection.ConnectionPool;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.DtoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class MenuDao implements Dao<MenuDto, MenuTable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuDao.class);

    @Override
    public synchronized void add(MenuDto dish) throws DaoException {
        try {
            addDishToMenu(dish);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void addDishToMenu(MenuDto dish) throws SQLException {
        ConnectionPool.connect(getAddQuery(), preparedStatement ->
                executeAddQuery(preparedStatement, dish));
    }

    private String getAddQuery() {
        String fields = String.join(", ", MenuTable.DISH_NAME.get(),
                MenuTable.CATEGORY.get(), MenuTable.DESCRIPTION.get(),
                MenuTable.PRICE.get());
        return "INSERT INTO " + MenuTable.TABLE_NAME.get() +
                " (" + fields + ") VALUES (?,?,?,?)";
    }

    private void executeAddQuery(PreparedStatement ps, MenuDto dish) throws SQLException {
        ps.setString(1, dish.getName());
        ps.setString(2, dish.getCategory());
        ps.setString(3, dish.getDescription());
        ps.setDouble(4, dish.getPrice());
        LOGGER.info(ps.executeUpdate() + " dish has been added. Dish name:" + dish.getName());
    }

    private void handleException(Exception ex) throws DaoException {
        LOGGER.warn(ex.toString());
        throw new DaoException(ex);
    }

    @Override
    public synchronized void update(MenuTable mf, MenuDto dish) throws DaoException {
        String value = getUpdatableField(mf, dish);
        int dishId = dish.getId();
        try {
            updateField(mf, value, dishId);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private String getUpdatableField(MenuTable mf, MenuDto dish) {
        Map<MenuTable, String> newValues = new EnumMap<>(MenuTable.class);
        newValues.put(MenuTable.DISH_NAME, dish.getName());
        newValues.put(MenuTable.CATEGORY, dish.getCategory());
        newValues.put(MenuTable.DESCRIPTION, dish.getDescription());
        newValues.put(MenuTable.PRICE, String.valueOf(dish.getPrice()));
        return newValues.get(mf);
    }

    private void updateField(MenuTable field, String value,
                             int dishId) throws SQLException {
        ConnectionPool.connect(getUpdateQuery(field),
                preparedStatement -> executeUpdateQuery(preparedStatement, value, dishId));
    }

    private String getUpdateQuery(MenuTable field) {
        return "UPDATE " + MenuTable.TABLE_NAME.get() + " SET " + field + " = ? WHERE " +
                MenuTable.DISH_ID + " = ?";
    }

    private void executeUpdateQuery(PreparedStatement ps,
                                    String value, int dishId) throws SQLException {
        ps.setString(1, value);
        ps.setInt(2, dishId);
        LOGGER.debug(ps.executeUpdate() + " dish has been updated. Dish id: " + dishId);
    }

    @Override
    public MenuDto get(MenuDto menu) throws DaoException {
        List<MenuDto> dishes = new ArrayList<>();
        String category = menu.getCategory();
        try {
            getDishes(category, dishes);
        } catch (SQLException ex) {
            handleException(ex);
        }
        return DtoFactory.getDishDto(dishes);
    }

    private void getDishes(String category, List<MenuDto> menu)
            throws SQLException {
        ConnectionPool.connect(getGetQuery(category),
                preparedStatement -> executeGetQuery(preparedStatement, menu));
    }

    //TODO transfer all queries to storage procedures
    //TODO create abstract class for methods
    private String getGetQuery(String category) {
        String fields = defineFields();
        if (category.equals(MenuCategories.WHOLE_MENU.get())) {
            return madeQuery(fields);
        } else {
            return madeParametrizedQuery(fields, category);
        }
    }

    private String defineFields() {
        return String.join(", ", MenuTable.DISH_ID.get(),
                MenuTable.DISH_NAME.get(), MenuTable.CATEGORY.get(),
                MenuTable.DESCRIPTION.get(), MenuTable.PRICE.get(),
                ImageTable.IMAGE_PATH.get());
    }

    private String madeQuery(String fields) {
        return "SELECT " + fields + " FROM " + MenuTable.TABLE_NAME.get() +
                "LEFT JOIN " + ImageTable.TABLE_NAME.get() + " USING (" +
                MenuTable.DISH_ID + ")";
    }

    private String madeParametrizedQuery(String fields, String category) {
        return "SELECT " + fields + " FROM " + MenuTable.TABLE_NAME.get() +
                "LEFT JOIN " + MenuTable.TABLE_NAME.get() + " USING (" +
                MenuTable.DISH_ID + ") WHERE " + MenuTable.CATEGORY +
                " = '" + category + "'";
    }

    private void executeGetQuery(PreparedStatement ps,
                                 List<MenuDto> menu) throws SQLException {
        ps.executeQuery();
        try (ResultSet rs = ps.getResultSet()) {
            collectDtoDishes(rs, menu);
        }
    }

    private void collectDtoDishes(ResultSet rs, List<MenuDto> menu) throws SQLException {
        while (rs.next()) {
            menu.add(collectDishes(rs));
        }
    }

    private MenuDto collectDishes(ResultSet rs) throws SQLException {
        MenuDto dish = DtoFactory.getDishDto(scanDishesFields(rs));
        dish.setImages(getDishImages(rs));
        return dish;
    }

    private Map<MenuTable, String> scanDishesFields(ResultSet rs) throws SQLException {
        Map<MenuTable, String> param = new EnumMap<>(MenuTable.class);
        ResultSetMetaData metaData = rs.getMetaData();
        putFieldsToMap(param, metaData, rs);
        return param;
    }

    private void putFieldsToMap(Map<MenuTable, String> param, ResultSetMetaData metaData,
                                ResultSet rs) throws SQLException {
        int columnsQuantity = metaData.getColumnCount();
        for (int i = 1; i <= columnsQuantity; i++) {
            MenuTable key = Enum.valueOf(MenuTable.class,
                    metaData.getColumnName(i).toUpperCase());
            String value = rs.getString(i);
            param.put(key, value);
        }
    }

    private List<String> getDishImages(ResultSet rs) throws SQLException {
        List<String> images = new ArrayList<>();
        int dishId = rs.getInt(MenuTable.DISH_ID.get());
        rs.previous();
        collectImages(images, rs, dishId);
        return images;
    }

    private void collectImages(List<String> images, ResultSet rs,
                               int dishId) throws SQLException {
        while (rs.next() && dishId == rs.getInt(MenuTable.DISH_ID.get())) {
            images.add(rs.getString(ImageTable.IMAGE_PATH.get()));
        }
        rs.previous();
    }

    @Override
    public synchronized void delete(MenuDto dish) throws DaoException {
        int dishId = dish.getId();
        try {
            deleteDish(dishId);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void deleteDish(int dishId) throws SQLException {
        ConnectionPool.connect(getDeleteQuery(),
                preparedStatement -> executeDeleteQuery(preparedStatement, dishId));
    }

    private String getDeleteQuery() {
        return "DELETE FROM " + MenuTable.TABLE_NAME.get() + " WHERE " +
                MenuTable.DISH_ID + " = ?";
    }

    private void executeDeleteQuery(PreparedStatement ps,
                                    int dishId) throws SQLException {
        ps.setInt(1, dishId);
        LOGGER.info(ps.executeUpdate() + " dish has been deleted. dishId: " + dishId);
    }
}
