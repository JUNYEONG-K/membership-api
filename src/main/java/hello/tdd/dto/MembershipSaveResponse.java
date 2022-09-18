package hello.tdd.dto;

import hello.tdd.domain.MembershipType;
import lombok.*;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MembershipSaveResponse {

    private Long id;
    private MembershipType membershipType;
    private Integer amountPoint;
}
