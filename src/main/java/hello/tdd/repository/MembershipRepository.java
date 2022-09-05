package hello.tdd.repository;

import hello.tdd.domain.Membership;
import hello.tdd.domain.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);
}
