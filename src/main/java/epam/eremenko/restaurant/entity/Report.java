package epam.eremenko.restaurant.entity;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Report implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Order> orders;
    private int ordersQuantity;

    public Report() {
    }

    public Report(List<Order> orders) {
        this.orders = orders;
        setOrdersQuantity(orders.size());
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getOrdersQuantity() {
        return ordersQuantity;
    }

    public void setOrdersQuantity(int ordersQuantity) {
        this.ordersQuantity = ordersQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        Report report = (Report) o;
        return ordersQuantity == report.ordersQuantity &&
                Objects.equals(orders, report.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders, ordersQuantity);
    }

    @Override
    public String toString() {
        return "Report{" +
                "orders=" + orders +
                ", ordersQuantity=" + ordersQuantity +
                '}';
    }
}
