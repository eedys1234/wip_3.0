package com.wip.bool.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.app.dto.AppVersionDto;
import com.wip.bool.app.service.AppVersionService;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.app.AppFactory;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AppVersionControllerTest {

    @InjectMocks
    private AppVersionController appVersionController;

    @Mock
    private AppVersionService appVersionService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(appVersionController).build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("app 정보 추가")
    @Test
    public void app_정보_추가_Controller() throws Exception {

        //given
        String name = "ILLECTORN";
        String version = "1.0.0.0";

        AppVersionDto.AppVersionSaveRequest requestDto = new AppVersionDto.AppVersionSaveRequest();
        ReflectionTestUtils.setField(requestDto, "name", name);
        ReflectionTestUtils.setField(requestDto, "version", version);

        doReturn(1L).when(appVersionService).saveApp(any(AppVersionDto.AppVersionSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/app/version")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        ApiResponse<Long> apiResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = apiResponse.getResult();
        assertThat(id).isEqualTo(1);

        //verify
        verify(appVersionService, times(1)).saveApp(any(AppVersionDto.AppVersionSaveRequest.class));
    }

    @DisplayName("app 정보 리스트 조회")
    @Test
    public void app_정보_리스트_조회_Controller() throws Exception {

        //given
        List<AppVersionDto.AppVersionResponse> appVersions = Arrays.asList(new AppVersionDto.AppVersionResponse(AppFactory.getAppVersion(1L)));
        doReturn(appVersions).when(appVersionService).gets();

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/app/versions"));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<List<AppVersionDto.AppVersionResponse>> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<List<AppVersionDto.AppVersionResponse>>>() {});
        List<AppVersionDto.AppVersionResponse> values = response.getResult();
        assertThat(values.size()).isEqualTo(1);

        //verify
        verify(appVersionService, times(1)).gets();
    }

    @DisplayName("app 정보 조회")
    @Test
    public void app_정보_조회_Controller() throws Exception {

        //given
        String name = "ILLECTORNC";
        AppVersionDto.AppVersionResponse appVersion = new AppVersionDto.AppVersionResponse(AppFactory.getAppVersion(1L));
        doReturn(appVersion).when(appVersionService).get(anyString());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/app/version")
                .param("name", name));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<AppVersionDto.AppVersionResponse> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<AppVersionDto.AppVersionResponse>>() {});

        AppVersionDto.AppVersionResponse value = response.getResult();

        assertThat(value.getVersion()).isEqualTo(appVersion.getVersion());
        assertThat(value.getName()).isEqualTo(appVersion.getName());

        //verify
        verify(appVersionService, times(1)).get(anyString());
    }

    @DisplayName("app 정보 삭제")
    @Test
    public void app_정보_삭제_Controller() throws Exception {

        //given
        doReturn(1L).when(appVersionService).deleteApp(anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/app/version/1")
        .contentType(MediaType.APPLICATION_JSON));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long resValue = response.getResult();
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(appVersionService, times(1)).deleteApp(anyLong());
    }
 }
