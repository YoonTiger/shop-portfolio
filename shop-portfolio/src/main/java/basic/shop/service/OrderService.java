package basic.shop.service;

import basic.shop.entity.*;
import basic.shop.entity.enums.DeliveryStatus;
import basic.shop.entity.enums.OrderStatus;
import basic.shop.repository.ItemRepository;
import basic.shop.repository.MemberRepository;
import basic.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 단건 주문
     */
    public void saveOrder(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("없는 상품입니다"));

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);
        //순서 지켜야함
        orderRepository.saveDelivery(delivery);
        orderRepository.saveOrder(order);
        orderRepository.saveOrderItem(orderItem);
        itemRepository.update(item);
    }

    /**
     * 모든 주문 조회 (관리자 전용)
     */
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    /**
     * 내 주문들 조회
     */
    public List<Order> findByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }

    /**
     * 주문 취소
     */
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다"));
        order.cancel();

        List<OrderItem> orderItems = order.getOrderItems();

        for (OrderItem orderItem : orderItems) {
            orderItem.getItem().addStock(orderItem.getCount());
        }

        List<Item> items = orderItems.stream().map(OrderItem::getItem).collect(toList());

        itemRepository.updateAll(items);
        orderRepository.updateOrder(order);

    }

    /**
     * 주문 삭제
     */
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다"));
        if(order.getStatus().equals(OrderStatus.ORDER)){
            throw new IllegalStateException("주문중인 상품은 삭제가 불가능합니다. 주문을 취소하고 삭제해 주세요");
        }
        orderRepository.deleteDeliveryById(order.getDelivery().getId());
        orderRepository.delete(orderId);
    }



    /**
     * 내 판매 현황 조회
     */
    public List<Order> findSaleItemsByMemberId(Long id) {
        return orderRepository.findMySaleByMemberId(id);
    }

    /**
     * 내 판매 현황 상세
     */
    public Order findSaleItemByIds(Long memberId, Long orderId){
        return orderRepository.findSaleItemByIds(memberId, orderId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다"));
    }



    /**
     * 상품 발송 (관리자 전용)
     */
    public void sendItemByOrderId(Long orderId){
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다"));
        order.complete();
        order.getDelivery().setStatus(DeliveryStatus.COMP);
        orderRepository.updateOrder(order);
        orderRepository.updateDelivery(order.getDelivery());
    }





    /**
     * (1번의 쿼리)
     * 쿼리는 한방이지만 db 에서 row 가 여러줄나간다
     */
    public Order findByOrderId(Long id) {
        return orderRepository.findByOrderId(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문입니다"));
    }



    /**
     * (2번의 쿼리)
     * 일대일 관계를 한번에 조인으로 가져온후
     * 일대다 관계를 다시 조인한다
     */
    public List<Order> find1(Long id) {
        /**
         *  order 의 member 는 주문자
         *  orderItem 의 member 는 판매자
         */

        List<Order> orders = orderRepository.findByMemberId(id);

        List<Long> orderIds = orders.stream().map(Order::getId).collect(toList());

        List<OrderItem> orderItems = orderRepository.findOrderItemByOrderIds(orderIds);

        Map<Long, List<OrderItem>> orderItemMap = orderItems.stream().collect(groupingBy(oi->oi.getOrder().getId()));

        for (Order order : orders) {
            order.setOrderItems(orderItemMap.get(order.getId()));
        }
        //orders.forEach(o -> o.setOrderItems(orderItemMap.get(o.getId())));
        return orders;
    }



}
