package basic.shop.entity;

import basic.shop.entity.enums.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Delivery {

    private Long id;

    private Address address;

    private DeliveryStatus status;

}
