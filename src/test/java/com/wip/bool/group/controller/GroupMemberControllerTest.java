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

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(groupMemberController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("그룹멤버 추가")
    @Test
    public void 그룹멤버_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        Group group = GroupFactory.getGroup(user, 1L);
        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user, 1L);

        GroupMemberDto.GroupMemberSaveRequest requestDto = new GroupMemberDto.GroupMemberSaveRequest();
        ReflectionTestUtils.setField(requestDto, "groupId", 1L);
        doReturn(groupMember.getId()).when(groupMemberService).saveGroupMember(anyLong(), any(GroupMemberDto.GroupMemberSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/group/member")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isEqualTo(groupMember.getId());

        //verify
        verify(groupMemberService, times(1)).saveGroupMember(anyLong(), any(GroupMemberDto.GroupMemberSaveRequest.class));
    }

    @DisplayName("그룹멤버 탈퇴")
    @Test
    public void 그룹멤버_탈퇴_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        doReturn(1L).when(groupMemberService).deleteGroupMember(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/group/member/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(groupMemberService, times(1)).deleteGroupMember(anyLong(), anyLong());
    }

    @DisplayName("그룹멤버 조회")
    @Test
    public void 그룹멤버_조회_Controller() throws Exception {

        //given
        String order = "ASC";
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser(1L);
        Group group = GroupFactory.getGroup(user, 1L);
        GroupMember groupMember = GroupMemberFactory.getGroupMember(group, user, 1L);

        List<GroupMemberDto.GroupMemberResponse> groupMembers = new ArrayList<>();
        groupMembers.add(new GroupMemberDto.GroupMemberResponse(groupMember));

        doReturn(groupMembers).when(groupMemberService).findAllByGroup(anyLong(), anyString(), anyInt(), anyInt());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("order", order);
        params.add("size", String.valueOf(size));
        params.add("offset", String.valueOf(offset));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/group/1/members")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .params(params));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]['group_member_id']").value(groupMember.getId()))
                .andReturn();

        //verify
        verify(groupMemberService, times(1)).findAllByGroup(anyLong(), anyString(), anyInt(), anyInt());
    }
}
