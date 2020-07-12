package epam.eremenko.restaurant.tag;

import epam.eremenko.restaurant.dto.OrderDto;

import javax.servlet.jsp.tagext.BodyTagSupport;

public class Processor extends BodyTagSupport {
    private static final long serialVersionUID = 1L;

    private String key;
    private OrderDto order;
    private int dishId;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getDishId() {
        return dishId;
    }

    @Override
    public int doStartTag() {
        switch (key) {
            case "deleteDish" -> {deleteDish(); return chooseConstant();}
            case "cancelOrder" -> {return EVAL_BODY_INCLUDE;}
            default -> {return SKIP_BODY;}
        }
    }

    private void deleteDish() {
        recalculateTotalAmount();
        deleteDishFromOrder();
    }

    private void recalculateTotalAmount() {
        order.getDishes().stream().filter(d -> d.getId() == dishId)
                .forEach(d -> order.setTotalAmount(subtract(order.getTotalAmount(), d.getAmount())));
    }

    private double subtract(double x, double y) {
        return (double) Math.round((x - y) * 100d) / 100d;
    }

    private void deleteDishFromOrder() {
        order.getDishes().removeIf(d -> d.getId() == dishId);
    }

    private int chooseConstant() {
        if (orderContainsAnyPosition()) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_INCLUDE;
        }
    }

    private boolean orderContainsAnyPosition() {
        return order.getDishes().size() != 0;
    }
}


