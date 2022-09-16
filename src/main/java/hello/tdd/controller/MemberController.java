package hello.tdd.controller;

import hello.tdd.dto.MemberSaveRequest;
import hello.tdd.dto.MemberSaveResponse;
import hello.tdd.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public ResponseEntity<MemberSaveResponse> addMember(
            @RequestBody @Valid MemberSaveRequest memberSaveRequest) {
        MemberSaveResponse memberSaveResponse = memberService.addMember(memberSaveRequest.getName(), memberSaveRequest.getEmail(), memberSaveRequest.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(memberSaveResponse);
    }
}
