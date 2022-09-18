package hello.tdd.controller;

import hello.tdd.domain.Member;
import hello.tdd.dto.*;
import hello.tdd.service.MemberService;
import hello.tdd.service.MembershipServiceV2;
import hello.tdd.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MembershipControllerV2 {

    private final MembershipServiceV2 membershipService;
    private final TokenService tokenService;

    @PostMapping("/api/v2/memberships")
    public ResponseEntity<MembershipSaveResponse> addMembership(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid MembershipSaveRequest membershipSaveRequest) {

        Member member = tokenService.getMember(authorizationHeader);

        MembershipSaveResponse membershipSaveResponse
                = membershipService.addMembership(member, membershipSaveRequest.getMembershipType(), membershipSaveRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipSaveResponse);
    }

    @GetMapping("/api/v2/memberships")
    public ResponseEntity<MembershipListResponse> getMembershipList(
            @RequestHeader("Authorization") String authorizationHeader) {
        Member member = tokenService.getMember(authorizationHeader);
        return ResponseEntity.ok(membershipService.getMembershipList(member));
    }

    @GetMapping("/api/v2/memberships/{membershipId}")
    public ResponseEntity<MyMembershipResponse> getMembership(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long membershipId) {
        Member member = tokenService.getMember(authorizationHeader);
        return ResponseEntity.ok(membershipService.getMembership(membershipId, member));
    }

    @DeleteMapping("/api/v2/memberships/{membershipId}")
    public ResponseEntity<Void> removeMembership(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long membershipId) {
        Long memberId = tokenService.getMemberId(authorizationHeader);
        membershipService.removeMembership(membershipId, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v2/memberships/{membershipId}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long membershipId,
            @RequestBody @Valid MembershipAddRequest membershipAddRequest) {
        Long memberId = tokenService.getMemberId(authorizationHeader);
        membershipService.accumulateMembershipPoint(membershipId, memberId, membershipAddRequest.getPoint());
        return ResponseEntity.ok().build();
    }

    // addMembership 메서드와 accumulateMembershipPoint 를 합친 API
    @PostMapping("/api/v3/memberships")
    public ResponseEntity<MembershipSaveResponse> stackPoint(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid MembershipSaveRequest membershipSaveRequest) {
        Member member = tokenService.getMember(authorizationHeader);
        MembershipSaveResponse membershipSaveResponse = membershipService.stackPoint(member, membershipSaveRequest.getMembershipType(), membershipSaveRequest.getPoint());
        return ResponseEntity.status(HttpStatus.CREATED).body(membershipSaveResponse);
    }
}
