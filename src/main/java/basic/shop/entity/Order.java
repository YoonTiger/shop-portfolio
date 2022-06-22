package basic.shop.entity;

import basic.shop.entity.enums.DeliveryStatus;
import basic.shop.entity.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    private Long id;

    private LocalDateTime orderDate;
    private OrderStatus status;

    private Member member;
    private Delivery delivery;

    private List<OrderItem> orderItems = new ArrayList<>();



    public Order(Member member, Delivery delivery, LocalDateTime orderDate, OrderStatus status) {
        this.member= member;
        this.delivery = delivery;
        this.orderDate = orderDate;
        this.status = status;
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem orderItem) {
        Order order = new Order(member, delivery, LocalDateTime.now(), OrderStatus.ORDER);
        order.addOrderItem(orderItem);
        return order;
    }
    public static Order createOrderWithCart(Member member, Delivery delivery, List<OrderItem> orderItems) {
        Order order = new Order(member, delivery, LocalDateTime.now(), OrderStatus.ORDER);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }


    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void cancel() {
        if(this.status == OrderStatus.CANCEL){
            throw new IllegalStateException("이미 취소된 주문 입니다");
        }
        else if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("배송완료된 상품은 취소가 불가능합니다");
        }
        this.status = OrderStatus.CANCEL;
    }

    public void complete(){
        this.status = OrderStatus.COMP;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }



    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", member=" + member +
                ", delivery=" + delivery +
                ", orderItems=" + orderItems +
                '}';
    }
}
