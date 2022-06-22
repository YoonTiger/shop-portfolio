package basic.shop.controller;

import basic.shop.SessionConst;
import basic.shop.controller.Dto.CartItemDTO;
import basic.shop.entity.CartItem;
import basic.shop.entity.Member;
import basic.shop.service.CartService;
import basic.shop.service.MemberService;
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
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;

    /**
     * 장바구니에 상품 추가
     */
    @PostMapping("/{itemId}/addCart")
    public String addCart(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                          @RequestParam("count")Integer count, @PathVariable("itemId") Long itemId){
        cartService.addCart(loginMember.getId(), itemId, count);

        return "redirect:/item/list";
    }

    /**
     * 내 장바구니 목록
     */
    @GetMapping("/{memberId}/myCart")
    public String cartList(@PathVariable("memberId") Long memberId, Model model){

        List<CartItem> all = cartService.findCartItemsByMemberId(memberId);
        List<CartItemDTO> cartItems = all.stream().map(CartItemDTO::new).collect(toList());
        Member member = memberService.findById(memberId); //
        model.addAttribute("member", member);
        model.addAttribute("cartItems", cartItems);
        return "orderView/orderCart";
    }

    /**
     * 장바구니 상품 주문
     */
    @PostMapping("/{memberId}/myCart")
    public String orderAllCart(@PathVariable("memberId") Long memberId){
        cartService.orderCart(memberId);
        return "redirect:/order/myOrder";
    }

    /**
     * 장바구니 전부 취소
     */
    @GetMapping("/{memberId}/cancelCart")
    public String cancelCart(@PathVariable("memberId") Long memberId){
        cartService.cancelCart(memberId);
        return "redirect:/cart/" + memberId + "/myCart";
    }
}
