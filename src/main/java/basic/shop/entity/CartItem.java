package basic.shop.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    private Long id;

    private String name;
    private Integer price;
    private Integer count;
    private String sellerName;


    private Item item;
    private Member member;

    public CartItem(Member member,Item item, int count) {
        this.name = item.getName();
        this.price = item.getPrice();
        this.sellerName = item.getMember().getName();
        this.count = count;
        this.member = member;
        this.item = item;
    }

    public static CartItem createCartItem(Member member,Item item, int count){
        return new CartItem(member, item, count);
    }

}
