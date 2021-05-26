package com.wip.bool.group.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.dto.GroupDto;
import com.wip.bool.group.service.GroupService;
import com.wip.bool.user.domain.User;
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
public class GroupControllerTest {

    @InjectMocks
    private GroupController groupController;

    @Mock
    private GroupService groupService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("그룹추가")
    @Test
    public void 그룹추가_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Group group = GroupFactory.getGroup(user, 1L);
        GroupDto.GroupSaveRequest requestDto = new GroupDto.GroupSaveRequest();
        ReflectionTestUtils.setField(requestDto, "groupName", "그룹_A");
        doReturn(group.getId()).when(groupService).saveGroup(anyLong(), any(GroupDto.GroupSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/group")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userId", user.getId())
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(group.getId());

        //verify
        verify(groupService, times(1)).saveGroup(anyLong(), any(GroupDto.GroupSaveRequest.class));
    }

    @DisplayName("그룹수정")
    @Test
    public void 그룹수정_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Group group = GroupFactory.getGroup(user, 1L);
        GroupDto.GroupUpdateRequest requestDto = new GroupDto.GroupUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "groupName", "그룹_A");
        doReturn(group.getId()).when(groupService).updateGroup(anyLong(), anyLong(), any(GroupDto.GroupUpdateRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/group/1")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto))
        .header("userId", user.getId()));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(group.getId());

        //verify
        verify(groupService, times(1)).updateGroup(anyLong(), anyLong(), any(GroupDto.GroupUpdateRequest.class));
    }

    @DisplayName("그룹삭제")
    @Test
    public void 그룹삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        doReturn(1L).when(groupService).deleteGroup(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/1")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userId", user.getId()));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(groupService, times(1)).deleteGroup(anyLong(), anyLong());
    }

    @DisplayName("그룹 리스트 조회 by Master")
    @Test
    public void 그룹_리스트_조회_byMaster_Controller() throws Exception {

        //given
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser(1L);
        List<Group> groups = GroupFactory.getGroupsWithId(user);

        doReturn(groups.stream()
                .map(GroupDto.GroupResponse::new)
                .collect(Collectors.toList())).when(groupService).findAllByMaster(anyLong(), anyString(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("order", "ASC");
        params.add("offset", String.valueOf(offset));
        params.add("size", String.valueOf(size));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/master/groups")
        .header("userId", user.getId())
        .params(params)
        .contentType(MediaType.APPLICATION_JSON));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$.result").isArray())
                                                .andReturn();

        //verify
        verify(groupService, times(1)).findAllByMaster(anyLong(), anyString(), anyInt(), anyInt());
    }


    @DisplayName("그룹_리스트_조회")
    @Test
    public void 그룹_리스트_조회_Controller() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        int cnt = 10;
        String order = "ASC";

        User user = UserFactory.getNormalUser(1L);
        List<Group> groups = GroupFactory.getGroupsWithId(user);

        doReturn(groups.stream()
                        .map(GroupDto.GroupResponse::new)
                        .collect(Collectors.toList())).when(groupService).findAllByUser(anyLong(), anyString(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("order", order);
        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/groups")
        .header("userId", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$.result").isArray())
                                                .andReturn();

        //verify
        verify(groupService, times(1)).findAllByUser(anyLong(), anyString(), anyInt(), anyInt());

    }

}
