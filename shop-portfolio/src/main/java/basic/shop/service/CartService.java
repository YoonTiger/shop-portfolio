package basic.shop.service;

import basic.shop.entity.*;
import basic.shop.entity.enums.DeliveryStatus;
import basic.shop.repository.ItemRepository;
import basic.shop.repository.MemberRepository;
import basic.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    /**
     * 장바구니 상품 추가
     */
    public void addCart(Long memberId, Long itemId, int count){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("없는 상품입니다"));

        CartItem cartItem = CartItem.createCartItem(member, item, count);

        orderRepository.addCart(cartItem);
    }

    /**
     * 장바구니 상품 취소
     */
    public void cancelCart(Long memberId){
        orderRepository.deleteCartItemsByMemberId(memberId);
    }

    /**
     * 장바구니 상품목록 조회
     */
    public List<CartItem> findCartItemsByMemberId(Long id) {
        return orderRepository.findCartItemsByMemberId(id);
    }

    /**
     * 장바구니 상품 주문
     */
    public void orderCart(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다"));

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        List<CartItem> cartItems = orderRepository.findCartItemsByMemberId(memberId);

        if(cartItems.isEmpty()){
            throw new NoSuchElementException("장바구니에 상품이 없습니다");
        }
        List<OrderItem> orderItems = OrderItem.createOrderItems(cartItems);

        List<Item> items = orderItems.stream().map(OrderItem::getItem).collect(toList());

        Order order = Order.createOrderWithCart(member, delivery, orderItems);

        orderRepository.saveDelivery(delivery);
        orderRepository.saveOrder(order);
        orderRepository.saveOrderItems(orderItems);
        itemRepository.updateAll(items);
        orderRepository.deleteCartItemsByMemberId(memberId);
    }

}
