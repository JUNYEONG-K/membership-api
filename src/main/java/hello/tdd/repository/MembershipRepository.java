package hello.tdd.repository;

import hello.tdd.domain.Member;
import hello.tdd.domain.Membership;
import hello.tdd.domain.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);
    List<Membership> findAllByUserId(String userId);
    Membership findByMemberAndMembershipType(Member member, MembershipType membershipType);
    List<Membership> findAllByMember(Member member);
}
