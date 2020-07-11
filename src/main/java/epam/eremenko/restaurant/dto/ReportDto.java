package epam.eremenko.restaurant.dto;

import java.util.List;
import java.util.Objects;

public class ReportDto extends Dto {
    private List<OrderDto> orders;
    private int userId;

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
