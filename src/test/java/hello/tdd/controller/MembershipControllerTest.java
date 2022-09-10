package hello.tdd.controller;

import com.google.gson.Gson;
import hello.tdd.domain.MembershipType;
import hello.tdd.dto.MembershipSaveRequest;
import hello.tdd.dto.MembershipSaveResponse;
import hello.tdd.dto.MyMembershipResponse;
import hello.tdd.error.GlobalExceptionHandler;
import hello.tdd.error.MembershipErrorResult;
import hello.tdd.error.MembershipException;
import hello.tdd.service.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static hello.tdd.controller.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController target;

    @Mock
    private MembershipService membershipService;

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
    void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {
        // given
        String url = "/api/v1/memberships";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipSaveRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_포인트가null() throws Exception {
        //given
        String url = "/api/v1/memberships";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipSaveRequest(null, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_포인트가음수() throws Exception {
        // given
        String url = "/api/v1/memberships";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipSaveRequest(-1, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록실패_멤버십종류가Null() throws Exception {
        // given
        String url = "/api/v1/memberships";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipSaveRequest(10000, null)))
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

    @Test
    void 멤버십등록실패_MembershipService에서ErrorThrow() throws Exception {
        // given
        String url = "/api/v1/memberships";
        Mockito.doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("12345", MembershipType.NAVER, 10000);
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipSaveRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십등록성공() throws Exception {
        // given
        String url = "/api/v1/memberships";
        MembershipSaveResponse membershipSaveResponse = MembershipSaveResponse.builder()
                .id(-1L)
                .membershipType(MembershipType.NAVER)
                .build();
        Mockito.doReturn(membershipSaveResponse).when(membershipService).addMembership("12345", MembershipType.NAVER, 10000);
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipSaveRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());

        MembershipSaveResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MembershipSaveResponse.class);

        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 멤버십목록조회_사용자식별값이헤더에없음_실패() throws Exception {
        // given
        String url = "/api/v1/memberships";
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void 멤버십목록조회_성공() throws Exception {
        // given
        String url = "/api/v1/memberships";
        Mockito.doReturn(Arrays.asList(
                MyMembershipResponse.builder().build(),
                MyMembershipResponse.builder().build(),
                MyMembershipResponse.builder().build()
        )).when(membershipService).getMembershipList("12345");
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "12345")
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
