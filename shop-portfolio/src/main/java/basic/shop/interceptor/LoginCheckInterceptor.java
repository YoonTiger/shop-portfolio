package basic.shop.interceptor;

import basic.shop.SessionConst;
import basic.shop.controller.messageUtil.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("로그인 체크 인터셉터 실행");

        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String contextPath = request.getContextPath();

        log.info("URI = {}", requestURI);
        log.info("queryString = {}", queryString);
        log.info("contextPath = {}", contextPath);

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
            log.info("미인증 사용자 요청");
            MessageUtil.alertAndMovePage(response, "로그인이 필요합니다", "/login?redirectURL=" + requestURI);
//            response.sendRedirect("/login");
            return false;
        }
        return true;

    }
}
