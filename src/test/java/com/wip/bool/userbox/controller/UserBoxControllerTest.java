package com.wip.bool.userbox.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userbox.UserBoxFactory;
import com.wip.bool.user.domain.User;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.dto.UserBoxDto;
import com.wip.bool.userbox.service.UserBoxService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserBoxControllerTest {

    @InjectMocks
    private UserBoxController userBoxController;

    @Mock
    private UserBoxService userBoxService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userBoxController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("사용자박스 추가")
    @Test
    public void 사용자박스_추가_Controller() throws Exception {

        //given
        String userBoxName = "사용자박스_1";
        User user = UserFactory.getNormalUser(1);
        UserBox userBox = UserBoxFactory.getUserBox(user, userBoxName, 1);
        UserBoxDto.UserBoxSaveRequest requestDto = new UserBoxDto.UserBoxSaveRequest();
        ReflectionTestUtils.setField(requestDto, "userBoxName", userBoxName);
        doReturn(userBox.getId()).when(userBoxService).addUserBox(any(Long.class), any(UserBoxDto.UserBoxSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/userbox")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(userBox.getId());

        //verify
        verify(userBoxService, times(1)).addUserBox(any(Long.class), any(UserBoxDto.UserBoxSaveRequest.class));

    }
    
    @DisplayName("사용자박스 수정")
    @Test
    public void 사용자박스_수정_Controller() throws Exception {

        //given
        String userBoxName = "사용자박스_1";
        String updateUserBoxName = "사용자박스_2";
        User user = UserFactory.getNormalUser(1);
        UserBox userBox = UserBoxFactory.getUserBox(user, userBoxName, 1);
        UserBoxDto.UserBoxUpdateRequest requestDto = new UserBoxDto.UserBoxUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "userBoxName", updateUserBoxName);
        doReturn(userBox.getId()).when(userBoxService).updateUserBox(any(Long.class), any(Long.class), any(UserBoxDto.UserBoxUpdateRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/userbox/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(userBox.getId());

        //verify
        verify(userBoxService, times(1)).updateUserBox(any(Long.class), any(Long.class), any(UserBoxDto.UserBoxUpdateRequest.class));
    }

    @DisplayName("사용자박스 삭제")
    @Test
    public void 사용자박스_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1);
        doReturn(1L).when(userBoxService).deleteUserBox(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/userbox/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userBoxService, times(1)).deleteUserBox(any(Long.class), any(Long.class));
    }

    @DisplayName("사용자박스 리스트 조회 by User")
    @Test
    public void 사용자박스_리스트_조회_byUser_Controller() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser(1);

        List<UserBox> userBoxes = UserBoxFactory.getUserBoxesWithId(user);

        doReturn(userBoxes.stream()
        .map(UserBoxDto.UserBoxResponse::new)
        .collect(Collectors.toList())).when(userBoxService).findAllByUser(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("order", order);
        params.add("offset", String.valueOf(offset));
        params.add("size", String.valueOf(size));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user-userboxes")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['user_box_name']").value(userBoxes.get(0).getUserBoxName()))
                .andReturn();


        //verify
        verify(userBoxService, times(1)).findAllByUser(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("사용자박스 리스트 조회 by Dept")
    @Test
    public void 사용자박스_리스트_조회_byDept_Controller() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;
        long deptId = 1;
        User user = UserFactory.getNormalUser(1);

        List<UserBox> userBoxes = UserBoxFactory.getUserBoxesWithId(user);

        doReturn(userBoxes.stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList())).when(userBoxService).findAllByDept(any(Long.class), any(Long.class), any(String.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("deptId", String.valueOf(deptId));
        params.add("order", order);
        params.add("offset", String.valueOf(offset));
        params.add("size", String.valueOf(size));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/dept-userboxes")
                .header("userId", user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['user_box_name']").value(userBoxes.get(0).getUserBoxName()))
                .andReturn();


        //verify
        verify(userBoxService, times(1)).findAllByDept(any(Long.class), any(Long.class), any(String.class), any(Integer.class), any(Integer.class));
    }

    @DisplayName("사용자박스 리스트 조회 by Group")
    @Test
    public void 사용자박스_리스트_조회_byGroup_Controller() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;
        String groupId = "1,2,3";
        User user = UserFactory.getNormalUser(1);

        List<UserBox> userBoxes = UserBoxFactory.getUserBoxesWithId(user);

        doReturn(userBoxes.stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList())).when(userBoxService).findAllByGroup(any(Long.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("groupId", groupId);
        params.add("order", order);
        params.add("offset", String.valueOf(offset));
        params.add("size", String.valueOf(size));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/group-userboxes")
                .header("userId", user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['user_box_name']").value(userBoxes.get(0).getUserBoxName()))
                .andReturn();


        //verify
        verify(userBoxService, times(1)).findAllByGroup(any(Long.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class));

    }

    @DisplayName("사용자박스 리스트 조회 by Total")
    @Test
    public void 사용자박스_리스트_조회_byTotal_Controller() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser(1);

        List<UserBox> userBoxes = UserBoxFactory.getUserBoxesWithId(user);

        doReturn(userBoxes.stream()
                .map(UserBoxDto.UserBoxResponse::new)
                .collect(Collectors.toList())).when(userBoxService).findAllByTotal(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("order", order);
        params.add("offset", String.valueOf(offset));
        params.add("size", String.valueOf(size));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/total-userboxes")
                .header("userId", user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['user_box_name']").value(userBoxes.get(0).getUserBoxName()))
                .andReturn();


        //verify
        verify(userBoxService, times(1)).findAllByTotal(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));
    }

}
