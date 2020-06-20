package epam.eremenko.restaurant.dao.impl;

import epam.eremenko.restaurant.dao.table.ImageTable;
import epam.eremenko.restaurant.dao.table.MenuTable;
import epam.eremenko.restaurant.dto.DtoFactory;
import epam.eremenko.restaurant.dto.ImageDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ImageDao extends DaoImpl<ImageDto, ImageTable> {

    @Override
    public synchronized void doAdd(ImageDto images) throws SQLException {
        addImagesToDirectory(images);
    }

    private void addImagesToDirectory(ImageDto images)
            throws SQLException {
        connectionsPool.connect(getSqlQueryAddingImagesToDirectory(),
                preparedStatement -> executeAddQuery(preparedStatement, images));
    }

    private String getSqlQueryAddingImagesToDirectory() {
        String fields = String.join(", ", ImageTable.IMAGE_NAME.get(),
                ImageTable.IMAGE_PATH.get(), ImageTable.DISH_ID.get());
        return "INSERT INTO " + ImageTable.TABLE_NAME.get() + " (" + fields + ") VALUES (?,?,?)";
    }

    private void executeAddQuery(PreparedStatement ps, ImageDto image) throws SQLException {
        ps.setString(1, image.getName());
        ps.setString(2, image.getPath());
        ps.setInt(3, image.getDishId());
        getLogger().debug(ps.executeUpdate() + " image added. Image name: " + image.getName());
    }

    @Override
    public ImageDto doGet(ImageDto image) throws SQLException {
        List<String> imagePaths = new ArrayList<>();
        int dishId = image.getDishId();
        getImages(dishId, imagePaths);
        return DtoFactory.getImageDto(imagePaths);
    }

    private void getImages(int dishId,
                           List<String> imagePaths) throws SQLException {
        connectionsPool.connect(getSqlQueryGettingImages(),
                preparedStatement -> executeGetQuery(preparedStatement, imagePaths, dishId));
    }

    private String getSqlQueryGettingImages() {
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
    void doUpdate(ImageTable field, ImageDto image) throws SQLException {
        String value = getUpdatableFields(field, image);
        int imageId = image.getId();
        executeUpdateQuery(field, value, imageId);
    }

    private String getUpdatableFields(ImageTable field, ImageDto image) {
        Map<ImageTable, String> newValues = new EnumMap<>(ImageTable.class);
        newValues.put(ImageTable.IMAGE_NAME, image.getName());
        newValues.put(ImageTable.IMAGE_PATH, image.getPath());
        newValues.put(ImageTable.DISH_ID, String.valueOf(image.getDishId()));
        return newValues.get(field);
    }

    private void executeUpdateQuery(ImageTable field, String value,
                                    int dishId) throws SQLException {
        connectionsPool.connect(getSqlQueryUpdatingField(field),
                preparedStatement -> prepareUpdateQuery(preparedStatement, value, dishId));
    }

    private String getSqlQueryUpdatingField(ImageTable field) {
        return "UPDATE " + ImageTable.TABLE_NAME.get() + " SET " + field + " = ? WHERE " +
                ImageTable.IMAGE_ID + " = ?";
    }

    private void prepareUpdateQuery(PreparedStatement ps,
                                    String value, int dishId) throws SQLException {
        ps.setString(1, value);
        ps.setInt(2, dishId);
        getLogger().debug(ps.executeUpdate() + " image updated. Image id: " + dishId);
    }
}
