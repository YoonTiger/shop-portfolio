package basic.shop.service;

import basic.shop.entity.Item;
import basic.shop.entity.Member;
import basic.shop.entity.Order;
import basic.shop.entity.OrderItem;
import basic.shop.repository.ItemRepository;
import basic.shop.repository.MemberRepository;
import basic.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {

        Optional<Member> findMember = memberRepository.findByName(member.getName());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    public void delete(Long id) {
        List<Order> orders = orderRepository.findByMemberId(id);

        if(!orders.isEmpty()){
            List<Long> orderIds = orders.stream().map(Order::getId).collect(toList());

            List<OrderItem> orderItems = orderRepository.findOrderItemByOrderIds(orderIds);
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().addStock(orderItem.getCount());
            }

            List<Item> items = orderItems.stream().map(OrderItem::getItem).collect(toList());
            itemRepository.updateAll(items);

            List<Long> deliveryIds = orders.stream().map(o -> o.getDelivery().getId()).collect(toList());
            orderRepository.deleteDeliveryByIds(deliveryIds);

            memberRepository.delete(id);
        }
        else{
            memberRepository.delete(id);
        }
    }

    public Long update(Member member) {
        memberRepository.update(member);
        return member.getId();
    }


    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다"));
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }
}
