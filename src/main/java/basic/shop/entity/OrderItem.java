package basic.shop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    private Long id;

    private int orderPrice;
    private int count;
    private Item item;

    private Order order;

    public OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        item.removeStock(count);
        return new OrderItem(item, orderPrice, count);
    }

    public static List<OrderItem> createOrderItems(List<CartItem> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            orderItems.add(new OrderItem(cartItem.getItem(), cartItem.getPrice(),cartItem.getCount()));
            cartItem.getItem().removeStock(cartItem.getCount());
        }
        return orderItems;
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderPrice=" + orderPrice +
                ", count=" + count +
                ", item=" + item +
                ", order=" + order +
                '}';
    }
}
