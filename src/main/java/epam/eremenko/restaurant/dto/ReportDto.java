package epam.eremenko.restaurant.dto;

import epam.eremenko.restaurant.config.ReportTypes;

import java.util.List;
import java.util.Objects;

public class ReportDto extends Dto {
    private List<OrderDto> orders;
    private int userId;
    private ReportTypes type;

    public ReportDto(){}

    public List<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ReportTypes getType() {
        return type;
    }

    public void setType(ReportTypes type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportDto)) return false;
        ReportDto reportDto = (ReportDto) o;
        return userId == reportDto.userId &&
                Objects.equals(orders, reportDto.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders, userId);
    }

    @Override
    public String toString() {
        return "ReportDto{" +
                "orders=" + orders +
                ", userId=" + userId +
                '}';
    }
}
