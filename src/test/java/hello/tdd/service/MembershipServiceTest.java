package hello.tdd.service;

import hello.tdd.domain.Membership;
import hello.tdd.domain.MembershipType;
import hello.tdd.dto.MembershipSaveResponse;
import hello.tdd.dto.MyMembershipResponse;
import hello.tdd.error.MembershipErrorResult;
import hello.tdd.error.MembershipException;
import hello.tdd.repository.MembershipRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;
    private final Long membershipId = -1L;

    // test 대상
    @InjectMocks    // @Mock 또는 @Spy로 생성된 가짜 객체를 자동으로 주입시켜주는 어노테이션
    private MembershipService target;
    // 의존 객체
    @Mock   // Mock 객체를 만들어 반환해주는 어노테이션
    private MembershipRepository membershipRepository;

    @Test
    void 멤버십등록실패_이미존재함() {
        // given
        doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

        // when
        MembershipException result = assertThrows(MembershipException.class, () -> target.addMembership(userId, membershipType, point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    void 멤버십등록성공() {
        // given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));
        // when
        MembershipSaveResponse result = target.addMembership(userId, membershipType, point);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(membershipType);
        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    @Test
    void 멤버십목록조회() {
        // given
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipRepository).findAllByUserId(userId);
        // when
        List<MyMembershipResponse> result = target.getMembershipList(userId);
        // then
        assertThat(result.size()).isEqualTo(3);
    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(membershipType)
                .build();
    }

    @Test
    void 멤버십상세조회실패_존재하지않음() {
        // given
        doReturn(Optional.empty()).when(membershipRepository).findById(membershipId);
        // when
        MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, userId));
        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    void 멤버십상세조회실패_본인아님() {
        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
        // when
        MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, "not owner"));
        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    void 멤버십상세조회성공() {
        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);
        // when
        MyMembershipResponse result = target.getMembership(membershipId, userId);
        // then
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(point);
    }
}
