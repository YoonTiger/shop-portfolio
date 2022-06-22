package basic.shop.controller.Dto;

import basic.shop.entity.*;
import basic.shop.entity.enums.DeliveryStatus;
import basic.shop.entity.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private Long id;

    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

    private Member member;
    private DeliveryStatus deliveryStatus;

    private List<OrderItemDTO> orderItems;

    private Address address;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.address = order.getDelivery().getAddress();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.deliveryStatus = order.getDelivery().getStatus();
        this.member = order.getMember();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDTO::new).
                collect(Collectors.toList());
    }
}
