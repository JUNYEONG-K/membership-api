package hello.tdd.controller;

import hello.tdd.domain.Member;
import hello.tdd.dto.MemberLoginRequest;
import hello.tdd.dto.MemberLoginResponse;
import hello.tdd.service.LoginService;
import hello.tdd.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberLoginController {

    private final LoginService loginService;
    private final TokenService tokenService;

    @PostMapping("/api/v1/member/sessionlogin")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody @Valid MemberLoginRequest loginRequest, HttpServletRequest request) {

        Member member = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());

        HttpSession session = request.getSession();
        if (!session.isNew()) {
            session.invalidate();
            session = request.getSession();
        }
        session.setAttribute("loginId", member.getId());

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .email(loginRequest.getEmail())
                .build();
        return ResponseEntity.ok(memberLoginResponse);
    }

    @PostMapping("/api/v1/member/tokenlogin")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody @Valid MemberLoginRequest loginRequest) {
        Member member = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());

        String token = tokenService.createToken(member);
        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .email(member.getEmail())
                .token(token)
                .build();

        return ResponseEntity.ok(memberLoginResponse);
    }
}
