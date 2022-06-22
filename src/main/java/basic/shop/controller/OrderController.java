package basic.shop.controller;

import basic.shop.SessionConst;
import basic.shop.controller.Dto.ItemDTO;
import basic.shop.controller.Dto.OrderDTO;
import basic.shop.entity.*;
import basic.shop.service.ItemService;
import basic.shop.service.MemberService;
import basic.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final MemberService memberService;

    /**
     * 단건 상품 주문
     */
    @GetMapping("/{itemId}/add")
    public String addOrderForm (@PathVariable("itemId")Long itemId, Model model){
        Item item = itemService.findById(itemId);
        model.addAttribute("item", new ItemDTO(item));
        return "orderView/orderAdd";
    }
    @PostMapping("/{itemId}/add")
    public String addOrder(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           @PathVariable("itemId") Long itemId,
                           @RequestParam("count")Integer count) {

        orderService.saveOrder(loginMember.getId(), itemId, count);
        return "redirect:/item/list";
    }


    /**
     * 내 주문 모두 조회
     */
    @GetMapping("/myOrder")
    public String infoMyOrder(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              Model model) {
        List<Order> all = orderService.findByMemberId(loginMember.getId());
        List<OrderDTO> orders = all.stream().map(OrderDTO::new).collect(toList());
        model.addAttribute("orders", orders);
        return "orderView/orderMine";
    }

    /**
     * 주문 상세 정보
     */
    @GetMapping("/{orderId}/info")
    public String infoOrder(@PathVariable Long orderId, Model model) {
        Order findOrder = orderService.findByOrderId(orderId);
        model.addAttribute("order", new OrderDTO(findOrder));
        return "orderView/orderInfo";
    }

    /**
     * 주문 취소
     */
    @GetMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/order/myOrder";
    }

    /**
     * 주문 삭제
     */
    @GetMapping("/{orderId}/delete")
    public String deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);
        return "redirect:/order/myOrder";
    }

    /**
     * 내 판매 모두 조회
     */
    @GetMapping("/mySale")
    public String infoMySale(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             Model model){
        List<Order> all = orderService.findSaleItemsByMemberId(loginMember.getId());
        List<OrderDTO> orders = all.stream().map(OrderDTO::new).collect(toList());
        model.addAttribute("orders",orders);
        return "orderView/saleMine";
    }

    /**
     * 판매 상세 정보
     * 주문 정보 인터셉터와 걸린다
     * 일단 config 에서 뺴놓음
     */
    @GetMapping("/{orderId}/saleInfo")
    public String infoSale(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           @PathVariable Long orderId, Model model){

        Order findOrder = orderService.findSaleItemByIds(loginMember.getId(), orderId);
        model.addAttribute("order", new OrderDTO(findOrder));
        return "orderView/saleInfo";
    }






//
//    @ResponseBody
//    @GetMapping("/test")
//    public List<OrderDTO> test(@RequestParam("id") Long id) {
//        List<Order> orders = orderService.find1(id);
//        List<OrderDTO> ordersDTO = orders.stream().map(OrderDTO::new).collect(toList());
//
//        return ordersDTO;
//    }



}
