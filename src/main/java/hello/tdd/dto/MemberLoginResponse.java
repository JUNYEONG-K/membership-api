package hello.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class MemberLoginResponse {
    private String name;
    private String email;
    private String auth;

    private String token;
    private String refreshToken;
}
