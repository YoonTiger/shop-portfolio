package basic.shop.controller.Dto;


import basic.shop.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {

    private Long id;

    @NotBlank
    @Length(min = 1, max = 10)
    private String name;

    @NotBlank
    @Length(min = 1, max =10)
    private String password;

    @NotEmpty
    private String city;
    @NotEmpty
    private String street;
    @NotNull
    private Integer zipcode;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.password = member.getPassword();
        this.city = member.getAddress().getCity();
        this.street = member.getAddress().getStreet();
        this.zipcode = member.getAddress().getZipcode();
    }
}
