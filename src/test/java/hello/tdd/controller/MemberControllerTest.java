package hello.tdd.controller;

import com.google.gson.Gson;
import hello.tdd.dto.MemberSaveRequest;
import hello.tdd.dto.MemberSaveResponse;
import hello.tdd.error.GlobalExceptionHandler;
import hello.tdd.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @InjectMocks
    private MemberController target;
    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void MockMVC가Null아님() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void 멤버십등록실패_이메일형식이아님() throws Exception {
        // given
        String url = "/api/v1/members";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberSaveRequest("test", "notemail", "pwd")))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_이름이null() throws Exception {
        // given
        String url = "/api/v1/members";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberSaveRequest(null, "test@naver.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_이메일이null() throws Exception {
        // given
        String url = "/api/v1/members";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberSaveRequest("test", null, "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_비밀번호null() throws Exception {
        // given
        String url = "/api/v1/members";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberSaveRequest("test", "test@naver.com", null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버등록성공() throws Exception {
        // given
        String url = "/api/v1/members";
        MemberSaveResponse memberSaveResponse = MemberSaveResponse.builder()
                .id(-1L)
                .build();
        Mockito.doReturn(memberSaveResponse).when(memberService).addMember("test", "test@naver.com", "1234");
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberSaveRequest("test", "test@naver.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private MemberSaveRequest memberSaveRequest(String name, String email, String pwd) {
        return MemberSaveRequest.builder()
                .name(name)
                .email(email)
                .password(pwd)
                .build();
    }
}
