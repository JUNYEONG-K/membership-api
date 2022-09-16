package hello.tdd.controller;

import hello.tdd.dto.MemberLoginRequest;
import hello.tdd.dto.MemberLoginResponse;
import hello.tdd.service.SessionLoginService;
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

    private final SessionLoginService sessionLoginService;

    @PostMapping("/api/v1/member/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody @Valid MemberLoginRequest loginRequest, HttpServletRequest request) {

        Long loginId = sessionLoginService.login(loginRequest.getEmail(), loginRequest.getPassword());

        HttpSession session = request.getSession();
        if (!session.isNew()) {
            session.invalidate();
            session = request.getSession();
        }
        session.setAttribute("loginId", loginId);

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .email(loginRequest.getEmail())
                .build();
        return ResponseEntity.ok(memberLoginResponse);
    }
}
