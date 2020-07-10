package epam.eremenko.restaurant.entity;

import epam.eremenko.restaurant.dto.MenuDto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<MenuDto> dishes;
    private int dishesQuantity;

    Menu(){}

    Menu(List<MenuDto> dishes){
        this.dishes = dishes;
        setDishesQuantity(dishes.size());
    }

    public List<MenuDto> getDishes() {
        return dishes;
    }

    public void setDishes(List<MenuDto> dishes) {
        this.dishes = dishes;
    }

    public int getDishesQuantity() {
        return dishesQuantity;
    }

    public void setDishesQuantity(int dishesQuantity) {
        this.dishesQuantity = dishesQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(dishes, menu.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishes, dishesQuantity);
    }
}
