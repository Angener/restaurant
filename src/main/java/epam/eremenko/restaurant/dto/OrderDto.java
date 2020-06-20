package epam.eremenko.restaurant.dto;

import java.sql.Timestamp;
import java.util.List;

public class OrderDto extends Dto{
    private List<MenuDto> dishes;
    private Timestamp orderDate;
    private double totalAmount;
    private int userId;
    private boolean isApproved;
    private boolean isPassed;
    private boolean isCooked;
    private boolean isBilled;
    private boolean isPaid;

    public OrderDto(){}

    public List<MenuDto> getDishes() {
        return dishes;
    }

    public void setDishes(List<MenuDto> dishes) {
        this.dishes = dishes;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setIsPassed(boolean passed) {
        isPassed = passed;
    }

    public boolean isCooked() {
        return isCooked;
    }

    public void setIsCooked(boolean cooked) {
        isCooked = cooked;
    }

    public boolean isBilled() {
        return isBilled;
    }

    public void setIsBilled(boolean billed) {
        isBilled = billed;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "dishes=" + dishes +
                ", orderId=" + getId() +
                ", ordered=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", userId=" + userId +
                ", isApproved=" + isApproved +
                ", isPassed=" + isPassed +
                ", isCooked=" + isCooked +
                ", isBilled=" + isBilled +
                ", isPaid=" + isPaid +
                '}';
    }
}
