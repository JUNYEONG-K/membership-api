package hello.tdd.service;

import hello.tdd.domain.Member;
import hello.tdd.error.MemberErrorResult;
import hello.tdd.error.MemberException;
import hello.tdd.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    private final String name = "test";
    private final String email = "test@naver.com";
    private final String pwd = "1234";

    @InjectMocks
    private SessionLoginService target;
    @Mock
    private MemberRepository memberRepository;

    @Test
    void 로그인실패_아이디없음() {
        // given
        Mockito.doReturn(null).when(memberRepository).findByEmail(email);
        // when
        MemberException result = assertThrows(MemberException.class, () -> target.login(email, pwd));
        // then
        assertThat(result.getErrorResult()).isEqualTo(MemberErrorResult.NO_MEMBER_ID);
    }

    @Test
    void 로그인실패_비밀번호안맞음() {
        // given
        Mockito.doReturn(member()).when(memberRepository).findByEmail(email);
        // when
        MemberException result = assertThrows(MemberException.class, () -> target.login(email, "no correct pwd"));
        // then
        assertThat(result.getErrorResult()).isEqualTo(MemberErrorResult.NO_PWD_CORRECT);
    }

    @Test
    void 로그인성공() {
        // given
        Mockito.doReturn(member()).when(memberRepository).findByEmail(email);
        // when
        Long loginId = target.login(email, pwd);
        // then
        assertThat(loginId).isEqualTo(-1L);
    }

    private Member member() {
        return Member.builder()
                .id(-1L)
                .name(name)
                .email(email)
                .password(pwd)
                .build();
    }
}
