package hello.tdd.dto;

import hello.tdd.domain.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class MyMembershipResponse {

    private final String memberEmail;
    private final Long membershipId;
    private final MembershipType membershipType;
    private final Integer point;
    private final LocalDateTime createdAt;
}
