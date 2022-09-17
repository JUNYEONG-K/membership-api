package hello.tdd.controller;

import com.google.gson.Gson;
import hello.tdd.dto.MemberLoginRequest;
import hello.tdd.dto.MemberLoginResponse;
import hello.tdd.error.GlobalExceptionHandler;
import hello.tdd.service.SessionLoginService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MemberLoginControllerTest {

    @InjectMocks
    private MemberLoginController target;

    @Mock
    private SessionLoginService sessionLoginService;

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
    void MockMvc가Null아님() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void 로그인실패_이메일형식아님() throws Exception{
        // given
        String url = "/api/v1/member/sessionlogin";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberLoginRequest("not email", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 로그인실패_이메일null() throws Exception {
        // given
        String url = "/api/v1/member/sessionlogin";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberLoginRequest(null, "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 로그인실패_비밀번호null() throws Exception {
        // given
        String url = "/api/v1/member/sessionlogin";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberLoginRequest("test@naver.com", null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 로그인성공() throws Exception {
        // given
        String url = "/api/v1/member/sessionlogin";

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(memberLoginRequest("test@naver.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

        MemberLoginResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MemberLoginResponse.class);

        assertThat(response.getEmail()).isEqualTo("test@naver.com");
    }

    private MemberLoginRequest memberLoginRequest(String email, String pwd) {
        return MemberLoginRequest.builder()
                .email(email)
                .password(pwd)
                .build();
    }
}

