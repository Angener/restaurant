package epam.eremenko.restaurant.config;

public enum OrderStatuses {
    PROCESSING("orders.status.placed"),
    APPROVED("orders.status.approved"),
    PREPARING("orders.status.preparing"),
    COOKED("orders.status.cooked"),
    PENDING_PAYMENT("orders.status.pendingPayment"),
    COMPLETED("orders.status.completed");

    private final String status;

    OrderStatuses(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
