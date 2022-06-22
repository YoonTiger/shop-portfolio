package basic.shop.entity;

import basic.shop.controller.Dto.ItemDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    private Long id;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private Member member;



    public Item(Member member, String name, int price, int stockQuantity) {
        this.member = member;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Item(Long id, String name, int price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }


    public static Item createItem(Member member, ItemDTO itemDTO) {
        return new Item(member, itemDTO.getName(), itemDTO.getPrice(), itemDTO.getStockQuantity());
    }
    public static Item updateItem(ItemDTO itemDTO) {
        return new Item(itemDTO.getId(), itemDTO.getName(), itemDTO.getPrice(), itemDTO.getStockQuantity());
    }


    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new RuntimeException("재고가 부족합니다");
        }
        this.stockQuantity =restStock;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", member=" + member +
                '}';
    }
}
