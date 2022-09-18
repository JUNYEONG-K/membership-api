package hello.tdd.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorResult {

    DUPLICATED_MEMBER_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Member Register Request"),
    NO_MEMBER_ID(HttpStatus.BAD_REQUEST, "Member ID doesn't exist"),
    NO_PWD_CORRECT(HttpStatus.BAD_REQUEST, "Password doesn't correct"),
    NO_TOKEN(HttpStatus.BAD_REQUEST, "No Token"),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Member Not Found"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
