package epam.eremenko.restaurant.entity;

import epam.eremenko.restaurant.attribute.OrderStatuses;
import epam.eremenko.restaurant.dao.table.OrderTable;
import epam.eremenko.restaurant.dto.MenuDto;
import epam.eremenko.restaurant.dto.OrderDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private List<MenuDto> dishes;
    private Timestamp orderDate;
    private double totalAmount;
    private int userId;
    private boolean isApproved;
    private boolean isPassed;
    private boolean isCooked;
    private boolean isBilled;
    private boolean isPaid;
    private OrderStatuses status;
    private OrderTable updatableColumn;

    public Order() {
    }

    public Order(OrderDto orderDto) {
        this.id = orderDto.getId();
        this.dishes = orderDto.getDishes();
        this.orderDate = orderDto.getOrderDate();
        this.totalAmount = orderDto.getTotalAmount();
        this.userId = orderDto.getUserId();
        this.isApproved = orderDto.isApproved();
        this.isPassed = orderDto.isPassed();
        this.isCooked = orderDto.isCooked();
        this.isBilled = orderDto.isBilled();
        this.isPaid = orderDto.isPaid();
        defineStatus();
    }

    private void defineStatus() {
        setStatus(chooseStatus(putBooleanFieldsWithTrueValuesToList()));
    }

    private List<Boolean> putBooleanFieldsWithTrueValuesToList() {
        List<Boolean> executionSteps = new ArrayList<>();
        executionSteps.add(isApproved);
        executionSteps.add(isPassed);
        executionSteps.add(isCooked);
        executionSteps.add(isBilled);
        executionSteps.add(isPaid);
        executionSteps.removeIf(s -> s.equals(false));
        return executionSteps;
    }

    private OrderStatuses chooseStatus(List<Boolean> executionSteps) {
        return switch (executionSteps.size()) {
            case 1 -> OrderStatuses.APPROVED;
            case 2 -> OrderStatuses.PREPARING;
            case 3 -> OrderStatuses.COOKED;
            case 4 -> OrderStatuses.PENDING_PAYMENT;
            case 5 -> OrderStatuses.COMPLETED;
            default -> OrderStatuses.PROCESSING;
        };
    }

    public void changeStatus(OrderStatuses status) {
        switch (status) {
            case APPROVED -> changeIsApproved();
            case PROCESSING -> changeIsPassed();
            case COOKED -> changeIsCooked();
            case PENDING_PAYMENT -> changeIsBilled();
            case COMPLETED -> changeIsPaid();
        }
    }

    private void changeIsApproved() {
        isApproved = true;
        updatableColumn = OrderTable.IS_APPROVED;
    }

    private void changeIsPassed() {
        isPassed = true;
        updatableColumn = OrderTable.IS_PASSED;
    }

    private void changeIsCooked() {
        isCooked = true;
        updatableColumn = OrderTable.IS_COOKED;
    }

    private void changeIsBilled() {
        isBilled = true;
        updatableColumn = OrderTable.IS_BILLED;
    }

    private void changeIsPaid() {
        isPaid = true;
        updatableColumn = OrderTable.IS_PAID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public OrderStatuses getStatus() {
        return status;
    }

    public void setStatus(OrderStatuses status) {
        this.status = status;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public boolean isCooked() {
        return isCooked;
    }

    public void setCooked(boolean cooked) {
        isCooked = cooked;
    }

    public boolean isBilled() {
        return isBilled;
    }

    public void setBilled(boolean billed) {
        isBilled = billed;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public OrderTable getUpdatableColumn() {
        return updatableColumn;
    }

    public void setUpdatableColumn(OrderTable updatableField) {
        this.updatableColumn = updatableField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id &&
                Double.compare(order.totalAmount, totalAmount) == 0 &&
                userId == order.userId &&
                isApproved == order.isApproved &&
                isPassed == order.isPassed &&
                isCooked == order.isCooked &&
                isBilled == order.isBilled &&
                isPaid == order.isPaid &&
                Objects.equals(dishes, order.dishes) &&
                Objects.equals(orderDate, order.orderDate) &&
                status == order.status &&
                updatableColumn == order.updatableColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dishes, orderDate, totalAmount, userId, isApproved,
                isPassed, isCooked, isBilled, isPaid, status, updatableColumn);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", dishes=" + dishes +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", userId=" + userId +
                ", isApproved=" + isApproved +
                ", isPassed=" + isPassed +
                ", isCooked=" + isCooked +
                ", isBilled=" + isBilled +
                ", isPaid=" + isPaid +
                ", status=" + status +
                ", updatableField=" + updatableColumn +
                '}';
    }
}
