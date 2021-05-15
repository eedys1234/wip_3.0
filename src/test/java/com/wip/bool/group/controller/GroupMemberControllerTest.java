package com.wip.bool.group.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.group.GroupMemberFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupMember;
import com.wip.bool.group.dto.GroupMemberDto;
import com.wip.bool.group.service.GroupMemberService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GroupMemberControllerTest {

    @InjectMocks
    private GroupMemberController groupMemberController;

    @Mock
    private GroupMemberService groupMemberService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private User getNormalUser() {
        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Group getGroup(User user) {
        Group group = GroupFactory.getGroup(user);
        ReflectionTestUtils.setField(group, "id", 1L);
        return group;
    }

    private GroupMember getGroupMember(Group group, User user) {
        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user);
        ReflectionTestUtils.setField(groupMember, "id", 1L);
        return groupMember;
    }

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupMemberController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("그룹멤버 추가")
    @Test
    public void 그룹멤버_추가_Controller() throws Exception {

        //given
        GroupMemberDto.GroupMemberSaveRequest requestDto = new GroupMemberDto.GroupMemberSaveRequest();
        ReflectionTestUtils.setField(requestDto, "groupId", 1L);
        doReturn(1L).when(groupMemberService).saveGroupMember(any(Long.class), any(GroupMemberDto.GroupMemberSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/group/member")
        .header("userId", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(1L);

        //verify
        verify(groupMemberService, times(1)).saveGroupMember(any(Long.class), any(GroupMemberDto.GroupMemberSaveRequest.class));
    }

    @DisplayName("그룹멤버 탈퇴")
    @Test
    public void 그룹멤버_탈퇴_Controller() throws Exception {

        //given
        doReturn(1L).when(groupMemberService).deleteGroupMember(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/member/1")
        .header("userId", 1L)
        .contentType(MediaType.APPLICATION_JSON));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(groupMemberService, times(1)).deleteGroupMember(any(Long.class), any(Long.class));
    }

    @DisplayName("그룹멤버 조회")
    @Test
    public void 그룹멤버_조회_Controller() throws Exception {

        //given
        User user = getNormalUser();
        Group group = getGroup(user);
        GroupMember groupMember = getGroupMember(group, user);
        String order = "ASC";
        int size = 10;
        int offset = 0;

        List<GroupMemberDto.GroupMemberResponse> groupMembers = new ArrayList<>();
        groupMembers.add(new GroupMemberDto.GroupMemberResponse(groupMember));

        doReturn(groupMembers).when(groupMemberService).findAllByGroup(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("order", order);
        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/group/1/members")
        .contentType(MediaType.APPLICATION_JSON)
        .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['group_member_id']").value(1L))
                .andReturn();

        //verify
        verify(groupMemberService, times(1)).findAllByGroup(any(Long.class), any(String.class), any(Integer.class), any(Integer.class));
    }
}
