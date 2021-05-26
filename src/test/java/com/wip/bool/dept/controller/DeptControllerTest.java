package com.wip.bool.dept.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.ApiResponse;
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

    @DisplayName("부서 추가")
    @Test
    public void 부서_추가_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        Dept dept = DeptFactory.getDept(1L);
        DeptDto.DeptSaveRequest requestDto = new DeptDto.DeptSaveRequest();
        ReflectionTestUtils.setField(requestDto, "deptName", dept.getDeptName());

        doReturn(dept.getId()).when(deptService).saveDept(anyLong(), any(DeptDto.DeptSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dept")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header("userId", user.getId())
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();

        assertThat(id).isGreaterThan(0L);
        assertThat(id).isEqualTo(dept.getId());

        //verify
        verify(deptService, times(1)).saveDept(anyLong(), any(DeptDto.DeptSaveRequest.class));
    }

    @DisplayName("부서 수정")
    @Test
    public void 부서_수정_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        Dept dept = DeptFactory.getDept(1L);

        DeptDto.DeptUpdateRequest requestDto = new DeptDto.DeptUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "deptName", dept.getDeptName());

        doReturn(dept.getId()).when(deptService).updateDept(anyLong(), anyLong(), any(DeptDto.DeptUpdateRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/dept/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isGreaterThan(0L);

        //verify
        verify(deptService, times(1)).updateDept(anyLong(), anyLong(), any(DeptDto.DeptUpdateRequest.class));
    }

    @DisplayName("부서 삭제")
    @Test
    public void 부서_삭제_Controller() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        doReturn(1L).when(deptService).deleteDept(anyLong(), anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/dept/1")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(deptService, times(1)).deleteDept(anyLong(), anyLong());
    }

    @DisplayName("부서 리스트 조회")
    @Test
    private void 부서_리스트_조회_Controller() throws Exception {

        //given
        List<Dept> depts = DeptFactory.getDeptsWithId();

        doReturn(depts.stream().map(DeptDto.DeptResponse::new).collect(Collectors.toList())).when(deptService).findAll();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/depts"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print())
                                                .andExpect(status().isOk())
                                                .andExpect(jsonPath("$.result").isArray())
                                                .andExpect(jsonPath("$.result[0]['dept_name']").value(depts.get(0).getDeptName()))
                                                .andReturn();


        //verify
        verify(deptService, times(1)).findAll();

    }
}
