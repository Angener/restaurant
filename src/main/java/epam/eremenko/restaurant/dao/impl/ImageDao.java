package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.Dao;
import epam.eremenko.restaurant.dao.table.ImageTable;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dao.connection.ConnectionPool;
import epam.eremenko.restaurant.dao.exception.DaoException;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.ImageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageDao implements Dao<ImageDto, ImageTable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDao.class);

    @Override
    public synchronized void add(ImageDto images) throws DaoException {
        try {
            addImagesToDirectory(images);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void addImagesToDirectory(ImageDto images)
            throws SQLException {
        ConnectionPool.connect(getAddQuery(),
                preparedStatement -> executeAddQuery(preparedStatement, images));
    }

    private String getAddQuery() {
        String fields = String.join(", ", ImageTable.IMAGE_NAME.get(),
                ImageTable.IMAGE_PATH.get(), ImageTable.DISH_ID.get());
        return "INSERT INTO " + ImageTable.TABLE_NAME.get() +
                " (" + fields + ") VALUES (?,?,?)";
    }

    private void executeAddQuery(PreparedStatement ps, ImageDto image) throws SQLException {
        ps.setString(1, image.getImageName());
        ps.setString(2, image.getImagePath());
        ps.setInt(3, image.getDishId());
        LOGGER.debug(ps.executeUpdate() + " image added. Image name: " + image.getImageName());
    }

    private void handleException(Exception ex) throws DaoException {
        LOGGER.warn(ex.toString());
        throw new DaoException(ex);
    }


    @Override
    public void update(ImageTable field, ImageDto image) throws DaoException {
        String value = getUpdatableFields(field, image);
        int imageId = image.getImageId();
        try {
            updateField(field, value, imageId);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private String getUpdatableFields(ImageTable field, ImageDto image) {
        Map<ImageTable, String> newValues = new HashMap<>();
        newValues.put(ImageTable.IMAGE_NAME, image.getImageName());
        newValues.put(ImageTable.IMAGE_PATH, image.getImagePath());
        newValues.put(ImageTable.DISH_ID, String.valueOf(image.getDishId()));
        return newValues.get(field);
    }

    private void updateField(ImageTable field, String value,
                             int dishId) throws SQLException {
        ConnectionPool.connect(getUpdateQuery(field),
                preparedStatement -> executeUpdateQuery(preparedStatement, value, dishId));
    }

    private String getUpdateQuery(ImageTable field) {
        return "UPDATE " + ImageTable.TABLE_NAME.get() + " SET " + field + " = ? WHERE " +
                ImageTable.IMAGE_ID + " = ?";
    }

    private void executeUpdateQuery(PreparedStatement ps,
                                    String value, int dishId) throws SQLException {
        ps.setString(1, value);
        ps.setInt(2, dishId);
        LOGGER.debug(ps.executeUpdate() + " image updated. Image id: " + dishId);
    }

    @Override
    public ImageDto get(ImageDto image) throws DaoException {
        List<String> imagePaths = new ArrayList<>();
        int dishId = image.getDishId();
        try {
            getImages(dishId, imagePaths);
        } catch (SQLException ex) {
            handleException(ex);
        }
        return DtoFactory.getImageDto(imagePaths);
    }

    private void getImages(int dishId,
                           List<String> imagePaths) throws SQLException {
        ConnectionPool.connect(getGetQuery(),
                preparedStatement -> executeGetQuery(preparedStatement, imagePaths, dishId));
    }

    private String getGetQuery() {
        return "SELECT " + ImageTable.IMAGE_PATH + " FROM " + MenuTable.TABLE_NAME.get() +
                " `m` JOIN " + ImageTable.TABLE_NAME.get() + " `i` ON m." +
                MenuTable.DISH_ID + " = ?" + " WHERE m." + MenuTable.DISH_ID +
                " = i." + ImageTable.DISH_ID;
    }

    private void executeGetQuery(PreparedStatement ps,
                                 List<String> imagePaths, int dishId) throws SQLException {
        ps.setInt(1, dishId);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            imagePaths.add(rs.getString(ImageTable.IMAGE_PATH.get()));
        }
    }

    @Override
    public void delete(ImageDto image) throws DaoException {
        int imageId = image.getImageId();
        try {
            deleteDish(imageId);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    private void deleteDish(int imageId) throws SQLException {
        ConnectionPool.connect(getDeleteQuery(),
                preparedStatement -> executeDeleteQuery(preparedStatement, imageId));
    }

    private String getDeleteQuery() {
        return "DELETE FROM " + ImageTable.TABLE_NAME.get() + " WHERE " +
                ImageTable.IMAGE_ID + " = ?";
    }

    private void executeDeleteQuery(PreparedStatement ps,
                                    int imageId) throws SQLException {
        ps.setInt(1, imageId);
        LOGGER.debug(ps.executeUpdate() + " image deleted. Image id:" + imageId);
    }
}
