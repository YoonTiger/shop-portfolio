package basic.shop.controller;

import basic.shop.SessionConst;
import basic.shop.controller.Dto.LoginDTO;
import basic.shop.controller.messageUtil.MessageUtil;
import basic.shop.entity.Member;
import basic.shop.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {


    private final LoginService loginService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/info")
    public String info(){ return "infomation"; }

    @GetMapping("/login")
    public String memberLoginForm(@ModelAttribute("login") LoginDTO loginDTO){
        return "memberView/memberLogin";
    }

    @PostMapping("/login")
    public String memberLogin (@Validated @ModelAttribute("login") LoginDTO loginDTO, BindingResult bindingResult,
                               HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(defaultValue = "/") String redirectURL ) throws IOException {

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "memberView/memberLogin";
        }

        Member loginMember = loginService.LoginCheck(loginDTO.getName(), loginDTO.getPassword());

        if (loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "memberView/memberLogin";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        MessageUtil.alertAndMovePage(response, "로그인 되었습니다", redirectURL);
        return "home";
    }

    @GetMapping("/logout")
    public String memberLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
            MessageUtil.alertAndMovePage(response, "로그아웃 되었습니다", "/");
        }

        return "home";
    }
}
