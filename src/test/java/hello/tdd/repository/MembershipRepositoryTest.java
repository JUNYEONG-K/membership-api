package hello.tdd.repository;

import hello.tdd.domain.Membership;
import hello.tdd.domain.MembershipType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    void MembershipRepository가Null아님() {
        assertThat(membershipRepository).isNotNull();
    }


    @Test
    void 멤버십등록() {
        // given
        Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        // when
        Membership result = membershipRepository.save(membership);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);
    }

    @Test
    void 멤버십존재여부() {
        // given
        Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        // when
        Membership savedResult = membershipRepository.save(membership);
        Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);

        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(findResult.getPoint()).isEqualTo(10000);

        assertThat(savedResult).isEqualTo(findResult);
    }

    @Test
    void 멤버십조회_사이즈가0() {
        // given

        // when
        List<Membership> result = membershipRepository.findAllByUserId("userId");
        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void 멤버십조회_사이즈가2() {
        // given
        Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();
        Membership kakaoMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.KAKAO)
                .point(20000)
                .build();
        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);
        // when
        List<Membership> result = membershipRepository.findAllByUserId("userId");
        // then
        assertThat(result.size()).isEqualTo(2);
    }
}

