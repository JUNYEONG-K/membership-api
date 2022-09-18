package hello.tdd.service;

import hello.tdd.domain.Member;
import hello.tdd.domain.Membership;
import hello.tdd.domain.MembershipType;
import hello.tdd.dto.MembershipSaveResponse;
import hello.tdd.error.MembershipErrorResult;
import hello.tdd.error.MembershipException;
import hello.tdd.repository.MembershipRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MembershipServiceV2Test {
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;
    private final Long membershipId = -1L;

    @InjectMocks
    private MembershipServiceV2 target;

    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private RatePointService ratePointService;

    private Member member;

    @BeforeEach
    void init() {
        member = Member.builder().build();
    }
    @Test
    void 멤버십등록실패_이미존재함() {
        // given
        doReturn(Membership.builder().build()).when(membershipRepository).findByMemberAndMembershipType(member, membershipType);
        // when
        MembershipException result = assertThrows(MembershipException.class, () -> target.addMembership(member, membershipType, point));
        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    void 멤버십등록성공() {
        // given
        doReturn(null).when(membershipRepository).findByMemberAndMembershipType(member, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));
        // when
        MembershipSaveResponse result = target.addMembership(member, membershipType, point);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(membershipType);

    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .point(point)
                .membershipType(membershipType)
                .member(member)
                .build();
    }
}