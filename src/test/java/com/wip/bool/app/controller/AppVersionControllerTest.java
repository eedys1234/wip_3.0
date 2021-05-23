package com.wip.bool.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.app.dto.AppVersionDto;
import com.wip.bool.app.service.AppVersionService;
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
import static org.mockito.Mockito.doReturn;
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

        doReturn(1L).when(appVersionService).save(any(AppVersionDto.AppVersionSaveRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/app/version")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isCreated()).andReturn();
        Long id = Long.valueOf(mvcResult.getResponse().getContentAsString());

        assertThat(id).isEqualTo(1L);
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
        List<AppVersionDto.AppVersionResponse> values = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);

        assertThat(values.size()).isEqualTo(1);
    }

    @DisplayName("app 정보 조회")
    @Test
    public void app_정보_조회_Controller() throws Exception {

        //given
        String name = "ILLECTORNC";
        AppVersionDto.AppVersionResponse appVersion = new AppVersionDto.AppVersionResponse(AppFactory.getAppVersion(1L));
        doReturn(appVersion).when(appVersionService).get(any(String.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/app/version")
                .param("name", name));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        AppVersionDto.AppVersionResponse values = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                AppVersionDto.AppVersionResponse.class);

        assertThat(values.getVersion()).isEqualTo(appVersion.getVersion());
        assertThat(values.getName()).isEqualTo(appVersion.getName());
    }

    @DisplayName("app 정보 삭제")
    @Test
    public void app_정보_삭제_Controller() throws Exception {

        //given
        doReturn(1L).when(appVersionService).delete(any(Long.class));

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/app/version/1")
        .contentType(MediaType.APPLICATION_JSON));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        Long resValue = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(resValue).isEqualTo(1L);
    }
 }
