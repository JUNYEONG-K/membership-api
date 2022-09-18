package hello.tdd.service;

import hello.tdd.domain.Member;
import hello.tdd.domain.Membership;
import hello.tdd.domain.MembershipType;
import hello.tdd.dto.MembershipListResponse;
import hello.tdd.dto.MembershipSaveResponse;
import hello.tdd.dto.MyMembershipResponse;
import hello.tdd.error.MembershipErrorResult;
import hello.tdd.error.MembershipException;
import hello.tdd.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipServiceV2 {

    private final MembershipRepository membershipRepository;
    private final PointService ratePointService;

    public MembershipSaveResponse addMembership(Member member, MembershipType membershipType, Integer point) {
        // 중복 멤버십 존재여부 체크
        Membership result = membershipRepository.findByMemberAndMembershipType(member, membershipType);
        // 중복 멤버십이 저장된 경우
        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }
        // 중복 멤버십 없으면 저장!
        Membership membership = Membership.builder()
                .point(point)
                .membershipType(membershipType)
                .member(member)
                .build();
        Membership savedMembership = membershipRepository.save(membership);

        return MembershipSaveResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }

    public MembershipListResponse getMembershipList(Member member) {
        List<Membership> membershipList = membershipRepository.findAllByMember(member);

        List<MyMembershipResponse> memberships = membershipList.stream()
                .map(v -> MyMembershipResponse.builder()
                        .membershipId(v.getId())
                        .membershipType(v.getMembershipType())
                        .point(v.getPoint())
                        .createdAt(v.getCreatedAt())
                        .build()).toList();
        return MembershipListResponse.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .memberships(memberships)
                .build();
    }

    public MyMembershipResponse getMembership(Long membershipId, Member member) {
        Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        Membership membership = optionalMembership.orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if (!membership.getMember().getId().equals(member.getId())) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        return MyMembershipResponse.builder()
                .memberEmail(member.getEmail())
                .membershipId(membershipId)
                .membershipType(membership.getMembershipType())
                .point(membership.getPoint())
                .createdAt(membership.getCreatedAt())
                .build();
    }

    public void removeMembership(Long membershipId, Long memberId) {
        Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        Membership membership = optionalMembership.orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
        if (!membership.getMember().getId().equals(memberId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }
        membershipRepository.deleteById(memberId);
    }

    public void accumulateMembershipPoint(Long membershipId, Long memberId, int amount) {
        Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        Membership membership = optionalMembership.orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
        if (!membership.getMember().getId().equals(memberId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        int additionalAmount = ratePointService.calculateAmount(amount);

        membership.accumulatePoint(additionalAmount + membership.getPoint());
    }

    public MembershipSaveResponse stackPoint(Member member, MembershipType membershipType, Integer point) {
        Membership result = membershipRepository.findByMemberAndMembershipType(member, membershipType);
        int additionalAmount = ratePointService.calculateAmount(point);
        // 이미 있으면 포인트값만 바꾸기
        if (result != null) {
            result.accumulatePoint(additionalAmount + result.getPoint());
            return MembershipSaveResponse.builder()
                    .id(result.getId())
                    .membershipType(result.getMembershipType())
                    .amountPoint(result.getPoint())
                    .build();
        } else {
            // 없으면 새로 저장
            Membership membership = Membership.builder()
                    .point(additionalAmount)
                    .membershipType(membershipType)
                    .member(member)
                    .build();
            Membership savedMembership = membershipRepository.save(membership);

            return MembershipSaveResponse.builder()
                    .id(savedMembership.getId())
                    .membershipType(savedMembership.getMembershipType())
                    .amountPoint(savedMembership.getPoint())
                    .build();
        }
    }
}
