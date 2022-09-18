package hello.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MembershipListResponse {

    private Long memberId;
    private String memberName;
    private List<MyMembershipResponse> memberships;
}
