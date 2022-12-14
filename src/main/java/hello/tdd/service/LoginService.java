package hello.tdd.service;

import hello.tdd.domain.Member;
import hello.tdd.error.MemberErrorResult;
import hello.tdd.error.MemberException;
import hello.tdd.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final EncryptService encryptService;

    public Member login(String email, String pwd) {
        Member member = memberRepository.findByEmail(email);
        // member email 존재 X -> 에러
        if (member == null) {
            throw new MemberException(MemberErrorResult.NO_MEMBER_ID);
        }
        // 비밀번호 틀리면 에러
//        if (!member.getPassword().equals(pwd)) {
//            throw new MemberException(MemberErrorResult.NO_PWD_CORRECT);
//        }
        String password = encryptService.getEncrypt(pwd);
        if (!member.getPassword().equals(password)) {
            throw new MemberException(MemberErrorResult.NO_PWD_CORRECT);
        }
        // 세션 저장소에 저장할 사용자 pk
        return member;
    }
}
