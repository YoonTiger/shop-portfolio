package basic.shop.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class ExHandler {

    @ExceptionHandler(Exception.class)
    public String exHandle(Exception e, Model model){

        ErrorResult errorMessage = new ErrorResult(e.getMessage());
        model.addAttribute("ex", errorMessage);
        return "error/ex";
    }

}
