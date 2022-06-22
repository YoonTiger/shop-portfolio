package basic.shop.repository;

import basic.shop.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderRepository {

    void saveOrder(Order order);
    void updateOrder(Order order);
    void delete (Long id);
    Optional<Order> findByOrderId(Long id);
    List<Order> findAll();
    List<Order> findByMemberId(Long id);

    void saveOrderItem(OrderItem orderItem);
    void saveOrderItems(List<OrderItem> orderItems);
    Optional<OrderItem> findOrderItemByOrderId(Long id);
    List<OrderItem> findOrderItemByOrderIds(List<Long> orderIds);

    void addCart (CartItem cartItem);
    void deleteCartItemsByMemberId(Long id);
    List<CartItem> findCartItemsByMemberId(Long id);

    List<Order> findMySaleByMemberId(Long id);
    Optional<Order> findSaleItemByIds(@Param("memberId")Long memberId, @Param("orderId")Long orderId);

    void saveDelivery(Delivery delivery);
    void updateDelivery(Delivery delivery);
    void deleteDeliveryById(Long id);
    void deleteDeliveryByIds(List<Long> deliveryIds);

    void sendItemByOrderId(Long id);
}
