package hello.tdd.dto;

import hello.tdd.domain.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembershipSaveRequest {
    @NotNull
    @Min(0)
    private Integer point;

    @NotNull
    private MembershipType membershipType;
}
