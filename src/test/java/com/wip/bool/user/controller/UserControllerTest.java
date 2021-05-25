package com.wip.bool.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.dto.UserDto;
import com.wip.bool.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("회원가입")
    @Test
    public void 회원가입_Controller() throws Exception {

        //given
        User user = UserFactory.getRequestUser(1L);
        doReturn(user.getId()).when(userService).join(any(UserDto.UserSaveRequest.class));

        UserDto.UserSaveRequest requestDto = new UserDto.UserSaveRequest();
        ReflectionTestUtils.setField(requestDto, "email", user.getEmail());
        ReflectionTestUtils.setField(requestDto, "userPassword", user.getUserPassword());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
                                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                    .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(user.getId());

        //verify
        verify(userService, times(1)).join(any(UserDto.UserSaveRequest.class));
    }

    @DisplayName("사용자 수정")
    @Test
    public void 사용자_수정_Controller() throws Exception {

        //given
        Dept dept = DeptFactory.getDept(1L);
        User user = UserFactory.getRequestUser(1L);
        doReturn(user.getId()).when(userService).update(anyLong(), any(UserDto.UserUpdateRequest.class));

        UserDto.UserUpdateRequest requestDto = new UserDto.UserUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "deptId", dept.getId());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/1")
                                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(user.getId());

        //verify
        verify(userService, times(1)).update(anyLong(), any(UserDto.UserUpdateRequest.class));
    }

    @DisplayName("사용자 삭제")
    @Test
    public void 사용자_삭제_Controller() throws Exception {

        //given
        doReturn(1L).when(userService).delete(anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/1")
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userService, times(1)).delete(anyLong());
    }

    @DisplayName("사용자 승인")
    @Test
    public void 사용자_승인_Controller() throws Exception {

        //given
        doReturn(1L).when(userService).approve(anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/approval/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userService, times(1)).approve(anyLong());
    }

}
