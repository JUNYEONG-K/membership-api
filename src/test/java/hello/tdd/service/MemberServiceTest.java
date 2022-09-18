package hello.tdd.service;

import hello.tdd.domain.Member;
import hello.tdd.dto.MemberSaveResponse;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    private final String name = "test";
    private final String email = "test@naver.com";
    private final String pwd = "1234";
    private final Long memberId = -1L;

    @InjectMocks
    private MemberService target;
    @Mock
    private MemberRepository memberRepository;

    @Test
    void 멤버등록실패_이미존재함() {
        // given
        doReturn(Member.builder().build()).when(memberRepository).findByEmail(email);
        // when
        MemberException result = assertThrows(MemberException.class, () -> target.addMember(name, email, pwd));
        // then
        assertThat(result.getErrorResult()).isEqualTo(MemberErrorResult.DUPLICATED_MEMBER_REGISTER);
    }

    @Test
    void 멤버등록성공() {
        // given
        doReturn(null).when(memberRepository).findByEmail(email);
        doReturn(member()).when(memberRepository).save(any(Member.class));
        // when
        MemberSaveResponse result = target.addMember(name, email, pwd);
        // then
        assertThat(result.getId()).isNotNull();
    }

    private Member member() {
        return Member.builder()
                .id(memberId)
                .name(name)
                .email(email)
                .password(pwd)
                .build();
    }

    @Test
    void 멤버상세조회실패_존재하지않음() {
        // given
        doReturn(Optional.empty()).when(memberRepository).findById(memberId);
        // when
        MemberException result = assertThrows(MemberException.class, () -> target.getMember(memberId));
        // then
        assertThat(result.getErrorResult()).isEqualTo(MemberErrorResult.MEMBER_NOT_FOUND);
    }

    @Test
    void 멤버상세조회성공() {
        // given
        doReturn(Optional.of(member())).when(memberRepository).findById(memberId);
        // when
        Member result = target.getMember(memberId);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(pwd);
    }
}
