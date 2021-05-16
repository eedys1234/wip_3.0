package com.wip.bool.group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.group.GroupMemberFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
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

import java.util.ArrayList;
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

    private User getUser() {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Group getGroup(User user) {
        Group group = GroupFactory.getGroup(user);
        ReflectionTestUtils.setField(group, "id", 1L);
        return group;
    }

    private Group getGroup(String groupName, User user) {
        Group group = GroupFactory.getGroup(groupName, user);
        ReflectionTestUtils.setField(group, "id", 1L);
        return group;
    }

    private Group addGroup(String groupName, User user, Long id) {

        Group group = getGroup(groupName, user);
        ReflectionTestUtils.setField(group, "id", id);
        return group;
    }

    private GroupMember getGroupMember(Group group, User user, Long id) {
        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user);
        ReflectionTestUtils.setField(groupMember, "id", id);
        return groupMember;
    }


    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("그룹추가")
    @Test
    public void 그룹추가_Controller() throws Exception {

        //given
        User user = getUser();
        Group group = getGroup(user);
        GroupDto.GroupSaveRequest requestDto = new GroupDto.GroupSaveRequest();
        ReflectionTestUtils.setField(requestDto, "groupName", "테스트그룹_1");
        doReturn(group.getId()).when(groupService).saveGroup(any(Long.class), any(GroupDto.GroupSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/group")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userId", user.getId())
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(group.getId());

        //verify
        verify(groupService, times(1)).saveGroup(any(Long.class), any(GroupDto.GroupSaveRequest.class));
    }

    @DisplayName("그룹수정")
    @Test
    public void 그룹수정_Controller() throws Exception {

        //given
        User user = getUser();
        Group group = getGroup(user);
        GroupDto.GroupUpdateRequest requestDto = new GroupDto.GroupUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "groupName", "테스트그룹_1");
        doReturn(group.getId()).when(groupService).updateGroup(any(Long.class), any(Long.class), any(GroupDto.GroupUpdateRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/group/1")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto))
        .header("userId", user.getId()));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(group.getId());

        //verify
        verify(groupService, times(1)).updateGroup(any(Long.class), any(Long.class), any(GroupDto.GroupUpdateRequest.class));
    }

    @DisplayName("그룹삭제")
    @Test
    public void 그룹삭제_Controller() throws Exception {

        //given
        User user = getUser();
        doReturn(1L).when(groupService).deleteGroup(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/1")
        .contentType(MediaType.APPLICATION_JSON)
        .header("userId", user.getId()));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(resValue).isEqualTo(1L);
    }

    @DisplayName("그룹 리스트 조회 by Master")
    @Test
    public void 그룹_리스트_조회_byMaster_Controller() throws Exception {

        //given
        User user = getUser();
        List<Group> groups = new ArrayList<>();
        int size = 10;
        int offset = 0;
        int cnt = 10;

        for(int i=1;i<=cnt;i++)
        {
            String groupName = "테스트그룹_" + i;
            groups.add(addGroup(groupName, user, (long)i));
        }

        doReturn(groups.stream()
                .map(GroupDto.GroupResponse::new)
                .collect(Collectors.toList())).when(groupService).findAllByMaster(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));

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
                                                .andExpect(jsonPath("$").isArray())
                                                .andReturn();

    }


    @DisplayName("그룹_리스트_조회")
    @Test
    public void 그룹_리스트_조회_Controller() throws Exception {

        //given
        User user = getUser();
        List<Group> groups = new ArrayList<>();
        List<String> groupNames = new ArrayList<>();

        String order = "ASC";
        int size = 10;
        int offset = 0;
        int cnt = 10;

        for(int i=1;i<=cnt;i++)
        {
            String groupName = "테스트그룹_" + i;
            Group group = addGroup(groupName, user, (long)i);
            GroupMember groupMember = getGroupMember(group, user, (long)i);
            groupNames.add(groupName);
            groups.add(group);
        }

        doReturn(groups.stream()
                        .map(GroupDto.GroupResponse::new)
                        .collect(Collectors.toList())).when(groupService).findAllByUser(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));

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
                                                .andExpect(jsonPath("$").isArray())
                                                .andReturn();

        verify(groupService, times(1)).findAllByUser(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));

    }

}
