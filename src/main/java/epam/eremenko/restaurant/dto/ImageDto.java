package epam.eremenko.restaurant.dto;

import java.util.List;

public class ImageDto extends Dto {
    private List<String> images;
    private String name;
    private String path;
    private int dishId;

    public ImageDto(){}

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    @Override
    public String toString() {
        return "ImageDto{" +
                "images=" + images +
                ", imageId=" + getId() +
                ", imageName='" + name + '\'' +
                ", imagePath='" + path + '\'' +
                ", dishId=" + dishId +
                '}';
    }
}
