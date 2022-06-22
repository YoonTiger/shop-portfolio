package basic.shop.interceptor;

import basic.shop.SessionConst;
import basic.shop.controller.messageUtil.MessageUtil;
import basic.shop.entity.Item;
import basic.shop.entity.Member;
import basic.shop.service.ItemService;
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
public class ValidateItemInterceptor implements HandlerInterceptor {

    private final ItemService itemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("상품 등록 회원 체크 인터셉터 실행");

        String requestURI = request.getRequestURI();
        long itemId = Long.parseLong(requestURI.replaceAll("[^0-9]", ""));

        Item item = itemService.findById(itemId);

        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(!loginMember.getId().equals(item.getMember().getId())){
            MessageUtil.alertAndMovePage(response, "본인만 가능합니다", "/item/list");
            return false;
        }
        return true;

    }
}
