package basic.shop.entity;

import basic.shop.controller.Dto.MemberDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String street;
    private int zipcode;


    public Address(String city, String street, int zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public static Address createAddress(MemberDTO memberDTO) {
        return new Address(memberDTO.getCity(), memberDTO.getStreet(), memberDTO.getZipcode());
    }
}
