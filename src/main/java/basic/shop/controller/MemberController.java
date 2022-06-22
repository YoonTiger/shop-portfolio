package basic.shop.controller;

import basic.shop.SessionConst;
import basic.shop.controller.Dto.MemberDTO;
import basic.shop.controller.messageUtil.MessageUtil;
import basic.shop.entity.Address;
import basic.shop.entity.Member;
import basic.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String addMemberForm(@ModelAttribute("member") MemberDTO memberDTO) {
        return "memberView/memberJoin";
    }

    @PostMapping("/join")
    public String addMember(@Validated @ModelAttribute("member") MemberDTO memberDTO,
                            BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "memberView/memberJoin";
        }

        Address address = Address.createAddress(memberDTO);
        Member member = Member.createMember(memberDTO, address);
        memberService.join(member);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        MessageUtil.alertAndMovePage(response, member.getName() + "님 가입되었습니다","/");
        return "home";
    }


    @GetMapping("/info")
    public String memberInfo(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             Model model) {
        Member member = memberService.findById(loginMember.getId());
        model.addAttribute("member", new MemberDTO(member));
        return "memberView/memberInfo";
    }


    @GetMapping("/{id}/edit")
    public String editMemberForm(@PathVariable("id") Long id, Model model) {

        Member member = memberService.findById(id);
        model.addAttribute("member", new MemberDTO(member));
        return "memberView/memberEdit";
    }


    @PostMapping("/{id}/edit")
    public String editMember(@Validated @ModelAttribute("member") MemberDTO memberDTO, BindingResult bindingResult,
                             HttpServletResponse response) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "memberView/memberEdit";
        }

        Address address = Address.createAddress(memberDTO);
        Member member = Member.updateMember(memberDTO, address);
        memberService.update(member);
        MessageUtil.alertAndMovePage(response, "수정이 완료되었습니다", "/member/info");
        return "memberView/memberInfo";
    }

    @GetMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id, HttpServletRequest request,  HttpServletResponse response) throws IOException {

        memberService.delete(id);

        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        MessageUtil.alertAndMovePage(response, "탈퇴가 완료되었습니다", "/");
        return "home";
    }



}
