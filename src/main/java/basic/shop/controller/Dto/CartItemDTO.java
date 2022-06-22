package basic.shop.controller.Dto;

import basic.shop.entity.CartItem;
import basic.shop.entity.Item;
import basic.shop.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDTO {

    private Long id;

    private String name;
    private Integer price;
    private Integer count;
    private String sellerName;

    private Item item;
    private Member member;


    public CartItemDTO(CartItem cartItem) {

        this.name = cartItem.getName();
        this.price = cartItem.getPrice();
        this.count = cartItem.getCount();
        this.sellerName = cartItem.getSellerName();
        this.item = cartItem.getItem();
        this.member = cartItem.getMember();
    }

}
