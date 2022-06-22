package basic.shop.interceptor;

import basic.shop.SessionConst;
import basic.shop.controller.messageUtil.MessageUtil;
import basic.shop.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Slf4j
public class ValidateMemberInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        log.info("회원 체크 인터셉터 실행");

        String requestURI = request.getRequestURI();
        long memberId = Long.parseLong(requestURI.replaceAll("[^0-9]", ""));

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(memberId != loginMember.getId()){
            MessageUtil.alertAndMovePage(response, "본인만 가능합니다", "/");
            return false;
        }
        return true;
    }
}
