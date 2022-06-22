package basic.shop.controller;

import basic.shop.controller.Dto.MemberDTO;
import basic.shop.controller.Dto.OrderDTO;
import basic.shop.entity.Member;
import basic.shop.entity.Order;
import basic.shop.service.MemberService;
import basic.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final MemberService memberService;

    /**
     * 전체 회원 조회
     */
    @GetMapping("/memberList")
    public String memberList(Model model) {

        List<Member> all = memberService.findAll();
        List<MemberDTO> members = all.stream().map(MemberDTO::new).collect(Collectors.toList());
        model.addAttribute("members", members);
        return "memberView/memberList";
    }

    /**
     * 전체 주문 조회
     */
    @GetMapping("/orderList")
    public String listOrder(Model model) {

        List<Order> all = orderService.findAll();
        List<OrderDTO> orders = all.stream().map(OrderDTO::new).collect(toList());
        model.addAttribute("orders", orders);
        return "orderView/orderList";
    }

    /**
     * 주문 상세 정보
     */
    @GetMapping("/order/{id}/info")
    public String orderManager(@PathVariable Long id, Model model){

        Order findOrder = orderService.findByOrderId(id);
        model.addAttribute("order", new OrderDTO(findOrder));
        return "orderView/orderManager";
    }

    /**
     * 주문 취소
     */
    @GetMapping("/order/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId){

        orderService.cancelOrder(orderId);
        return "redirect:/admin/orderList";
    }

    /**
     * 주문 삭제
     */
    @GetMapping("/order/{orderId}/delete")
    public String deleteOrder(@PathVariable Long orderId){

        orderService.deleteOrder(orderId);
        return "redirect:/admin/orderList";
    }

    /**
     * 상품 발송
     */
    @GetMapping("/order/{orderId}/send")
    public String sendItem(@PathVariable Long orderId) {

        orderService.sendItemByOrderId(orderId);
        return "redirect:/admin/orderList";
    }

}
