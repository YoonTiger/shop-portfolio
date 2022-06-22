package basic.shop.interceptor;

import basic.shop.SessionConst;
import basic.shop.controller.messageUtil.MessageUtil;
import basic.shop.entity.Member;
import basic.shop.entity.Order;
import basic.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateOrderInterceptor implements HandlerInterceptor {

    private final OrderService orderService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("주문 체크 인터셉터 실행");
        String requestURI = request.getRequestURI();
        long orderId = Long.parseLong(requestURI.replaceAll("[^0-9]", ""));

        Order order = orderService.findByOrderId(orderId);

        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(!order.getMember().getId().equals(member.getId())){
            MessageUtil.alertAndMovePage(response, "본인만 가능합니다", "/");
            return false;
        }
        return true;

    }
}
