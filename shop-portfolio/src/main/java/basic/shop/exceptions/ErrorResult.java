package basic.shop.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResult {

    private String message;

    public ErrorResult(String message) {
        this.message = message;
    }
}
