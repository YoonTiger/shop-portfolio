package basic.shop.service;

import basic.shop.entity.Member;
import basic.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member LoginCheck (String name, String password){

        Member member = memberRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이디입니다"));
        if(member.getPassword().equals(password)){
            return member;
        }
        else{
            return null;
        }
    }
}
