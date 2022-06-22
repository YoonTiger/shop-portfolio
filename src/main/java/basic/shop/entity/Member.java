package basic.shop.entity;

import basic.shop.controller.Dto.MemberDTO;
import basic.shop.entity.enums.MemberRoll;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private Long id;

    private String name;
    private String password;
    private Address address;

    private MemberRoll roll;

    public Member(String name, String password, Address address, MemberRoll roll) {
        this.name= name;
        this.password = password;
        this.address = address;
        this.roll = roll;
    }
    public Member(Long id, String name, String password, Address address, MemberRoll roll) {
        this.id= id;
        this.name= name;
        this.password = password;
        this.address = address;
        this.roll = roll;
    }

    public static Member createMember(MemberDTO memberDTO, Address address) {
        return new Member(memberDTO.getName(), memberDTO.getPassword(), address, MemberRoll.MEMBER);
    }
    public static Member updateMember(MemberDTO memberDTO, Address address) {
        return new Member(memberDTO.getId(), memberDTO.getName(), memberDTO.getPassword(), address, MemberRoll.MEMBER);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", address=" + address +
                ", roll=" + roll +
                '}';
    }
}
