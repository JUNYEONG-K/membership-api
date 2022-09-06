package hello.tdd.service;

import hello.tdd.domain.Membership;
import hello.tdd.domain.MembershipType;
import hello.tdd.dto.MembershipSaveResponse;
import hello.tdd.error.MembershipErrorResult;
import hello.tdd.error.MembershipException;
import hello.tdd.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipSaveResponse addMembership(String userId, MembershipType membershipType, Integer point) {
        // 중복 멤버십 존재 여부 체크
        Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        // 중복 멤버십이 저장된 경우, 에러 throw
        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }
        // 중복 멤버십이 없으면, 저장!
        Membership membership = Membership.builder()
                .userId(userId)
                .point(point)
                .membershipType(membershipType)
                .build();

        Membership savedMembership = membershipRepository.save(membership);

        return MembershipSaveResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }
}
