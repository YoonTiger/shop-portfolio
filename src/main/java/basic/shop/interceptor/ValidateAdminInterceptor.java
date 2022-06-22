package basic.shop.interceptor;

import basic.shop.SessionConst;
import basic.shop.controller.messageUtil.MessageUtil;
import basic.shop.entity.Member;
import basic.shop.entity.enums.MemberRoll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class ValidateAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("관리자 인증 인터셉터 실행");

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(!loginMember.getRoll().equals(MemberRoll.ADMIN)){
            MessageUtil.alertAndMovePage(response, "관리자만 이용 가능합니다", "/");
            return false;
        }
        return true;
    }
}
