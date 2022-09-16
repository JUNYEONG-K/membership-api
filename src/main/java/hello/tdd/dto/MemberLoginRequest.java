package hello.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class MemberLoginRequest {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
