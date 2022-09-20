package hello.tdd.service;

import hello.tdd.domain.Member;
import hello.tdd.dto.MemberSaveResponse;
import hello.tdd.error.MemberErrorResult;
import hello.tdd.error.MemberException;
import hello.tdd.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EncryptService encryptService;

    public MemberSaveResponse addMember(String name, String email, String pwd) {
        // 중복 멤버 존재 여부 체크
        Member result = memberRepository.findByEmail(email);
        // 중복 멤버있는 경우, 에러 throw
        if (result != null) {
            throw new MemberException(MemberErrorResult.DUPLICATED_MEMBER_REGISTER);
        }
        // 중복 멤버십 없으면 저장!
        String password = encryptService.getEncrypt(pwd);
        Member member = Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberSaveResponse.builder()
                .id(savedMember.getId())
                .build();
    }

    public Member getMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new MemberException(MemberErrorResult.MEMBER_NOT_FOUND));

        return member;
    }
}
