package epam.eremenko.restaurant.attribute;

public enum ReportTypes {

    ACTUAL_USER_ORDERS("actual_user_orders"),
    INCOMPLETE_ORDERS("incomplete_orders"),
    UNAPPROVED_ORDERS("unapproved_orders"),
    UNSENT_TO_KITCHEN_ORDERS("unsent_to_kitchen_orders"),
    UNCOOKED_ORDERS("uncooked_orders"),
    NOT_BILLED_ORDERS("not_billed_orders"),
    COMPLETED_ORDERS("completed_orders"),
    ALL_ORDERS("all_orders");


    private final String type;

    ReportTypes(String type) {
        this.type = type;
    }

    public String get() {
        return type;
    }
}
