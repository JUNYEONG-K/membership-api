package hello.tdd.controller;

import hello.tdd.dto.MembershipAddRequest;
import hello.tdd.dto.MembershipSaveRequest;
import hello.tdd.dto.MembershipSaveResponse;
import hello.tdd.dto.MyMembershipResponse;
import hello.tdd.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static hello.tdd.controller.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipSaveResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) String userId,
            @RequestBody @Valid MembershipSaveRequest membershipSaveRequest) {

        MembershipSaveResponse membershipSaveResponse = membershipService.addMembership(userId, membershipSaveRequest.getMembershipType(), membershipSaveRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipSaveResponse);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MyMembershipResponse>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) String userId) {
        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/memberships/{membershipId}")
    public ResponseEntity<MyMembershipResponse> getMembership(
            @RequestHeader(USER_ID_HEADER) String userId,
            @PathVariable Long membershipId) {
        return ResponseEntity.ok(membershipService.getMembership(membershipId, userId));
    }

    @DeleteMapping("/api/v1/memberships/{id}")
    public ResponseEntity<Void> removeMembership(
            @RequestHeader(USER_ID_HEADER) String userId,
            @PathVariable Long id) {
        membershipService.removeMembership(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/memberships/{id}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
            @RequestHeader(USER_ID_HEADER) String userId,
            @PathVariable Long id,
            @RequestBody @Valid MembershipAddRequest membershipAddRequest) {
        membershipService.accumulateMembershipPoint(id, userId, membershipAddRequest.getPoint());
        return ResponseEntity.ok().build();
    }
}
