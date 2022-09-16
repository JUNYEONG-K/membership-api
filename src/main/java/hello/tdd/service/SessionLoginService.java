package hello.tdd.service;

import hello.tdd.domain.Member;
import hello.tdd.error.MemberErrorResult;
import hello.tdd.error.MemberException;
import hello.tdd.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SessionLoginService {

    private final MemberRepository memberRepository;

    public Long login(String email, String pwd) {
        Member member = memberRepository.findByEmail(email);
        // member id 존재 X -> 에러
        if (member == null) {
            throw new MemberException(MemberErrorResult.NO_MEMBER_ID);
        }
        // 비밀번호 틀리면 에러
        if (!member.getPassword().equals(pwd)) {
            throw new MemberException(MemberErrorResult.NO_PWD_CORRECT);
        }
        // 세션 저장소에 저장할 사용자 pk
        return member.getId();
    }
}
