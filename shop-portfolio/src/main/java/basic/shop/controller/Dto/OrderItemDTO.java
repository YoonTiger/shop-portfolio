package basic.shop.controller.Dto;

import basic.shop.entity.Item;
import basic.shop.entity.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;
    private int orderPrice;
    private int count;
    private String name;
    int totalPrice;
    private Item item;


    public OrderItemDTO(OrderItem orderItem) {
        this.id =orderItem.getId();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
        this.name = orderItem.getItem().getName();
        this.totalPrice = orderPrice * count;
        this.item = orderItem.getItem();
    }
}
