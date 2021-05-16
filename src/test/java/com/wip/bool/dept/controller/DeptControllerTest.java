package com.wip.bool.dept.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.dto.DeptDto;
import com.wip.bool.dept.service.DeptService;
import com.wip.bool.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class DeptControllerTest {

    @InjectMocks
    private DeptController deptController;

    @Mock
    private DeptService deptService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(deptController).build();
        objectMapper = new ObjectMapper();
    }

    private User getAdminUser() {
        User user = UserFactory.getAdminUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Dept getDept() {
        Dept dept = DeptFactory.getDept();
        ReflectionTestUtils.setField(dept, "id", 1L);
        return dept;
    }

    private Dept getDept(String groupName, Long id) {
        Dept dept = DeptFactory.getDept(groupName);
        ReflectionTestUtils.setField(dept, "id", id);
        return dept;
    }

    @DisplayName("부서 추가")
    @Test
    public void 부서_추가_Controller() throws Exception {

        //given
        User user = getAdminUser();
        Dept dept = getDept();
        DeptDto.DeptSaveRequest requestDto = new DeptDto.DeptSaveRequest();
        ReflectionTestUtils.setField(requestDto, "deptName", dept.getDeptName());

        doReturn(dept.getId()).when(deptService).saveDept(any(Long.class), any(DeptDto.DeptSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dept")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header("userId", user.getId())
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(id).isGreaterThan(0L);

        //verify
        verify(deptService, times(1)).saveDept(any(Long.class), any(DeptDto.DeptSaveRequest.class));
    }

    @DisplayName("부서 수정")
    @Test
    public void 부서_수정_Controller() throws Exception {

        //given
        User user = getAdminUser();
        Dept dept = getDept();

        DeptDto.DeptUpdateRequest requestDto = new DeptDto.DeptUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "deptName", dept.getDeptName());

        doReturn(dept.getId()).when(deptService).updateDept(any(Long.class), any(Long.class), any(DeptDto.DeptUpdateRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/dept/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long id = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);
        assertThat(id).isGreaterThan(0L);

        //verify
        verify(deptService, times(1)).updateDept(any(Long.class), any(Long.class), any(DeptDto.DeptUpdateRequest.class));
    }

    @DisplayName("부서 삭제")
    @Test
    public void 부서_삭제_Controller() throws Exception {

        //given
        User user = getAdminUser();
        doReturn(1L).when(deptService).deleteDept(any(Long.class), any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/dept/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON));


        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(deptService, times(1)).deleteDept(any(Long.class), any(Long.class));
    }

    @DisplayName("부서 리스트 조회")
    @Test
    private void 부서_리스트_조회_Controller() throws Exception {

        //given
        int cnt = 4;
        String[] groupNames = {"밍공", "팡공", "맹공", "볼공"};
        List<Dept> depts = new ArrayList<>();

        for(int i=1;i<=cnt;i++)
        {
            Dept dept = getDept(groupNames[i-1], (long)i);
            depts.add(dept);
        }

        doReturn(depts.stream()
                    .map(DeptDto.DeptResponse::new)
                    .collect(Collectors.toList())).when(deptService).findAll();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/depts"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$").isArray())
                                                .andExpect(jsonPath("$[0]['dept_name']").value("밍공"))
                                                .andReturn();


        //verify
        verify(deptService, times(1)).findAll();

    }
}
