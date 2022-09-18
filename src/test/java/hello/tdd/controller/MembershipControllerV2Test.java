package hello.tdd.controller;

import com.google.gson.Gson;
import hello.tdd.domain.Member;
import hello.tdd.domain.MembershipType;
import hello.tdd.dto.MembershipSaveRequest;
import hello.tdd.error.GlobalExceptionHandler;
import hello.tdd.service.MembershipServiceV2;
import hello.tdd.service.TokenService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static hello.tdd.controller.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MembershipControllerV2Test {

    @InjectMocks
    private MembershipControllerV2 target;

    @Mock
    private MembershipServiceV2 membershipService;
    @Mock
    private TokenService tokenService;

    private MockMvc mockMvc;
    private Gson gson;
    private String token;
    private Member member;

    @BeforeEach
    void init() {
        gson = new Gson();
        member = Member.builder()
                .id(-1L)
                .name("test")
                .email("test@naver.com")
                .password("1234")
                .build();
        token = tokenService.createToken(member);
        System.out.println("token = " + token);
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void MockMvc가Null아님() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void 멤버십등록실패_토큰값없음() throws Exception {
        // given
        String url = "/api/v2/memberships";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(membershipSaveRequest(10000, MembershipType.NAVER)))
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_토큰이Bearer아님() throws Exception {
        //given
        String url = "/api/v2/memberships";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header("Authorization", "not Bearer")
                        .content(gson.toJson(membershipSaveRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_포인트가null() throws Exception {
        //given
        String url = "/api/v2/memberships";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header("Authorization", "Bearer xxx.yyy.zzz")
                        .content(gson.toJson(membershipSaveRequest(null, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private MembershipSaveRequest membershipSaveRequest(Integer point, MembershipType membershipType) {
        return MembershipSaveRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }


}