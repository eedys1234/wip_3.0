package com.wip.bool.userconfig.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userconfig.UserConfigFactory;
import com.wip.bool.user.controller.UserConfigController;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserConfig;
import com.wip.bool.user.dto.UserConfigDto;
import com.wip.bool.user.service.UserConfigService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserConfigControllerTest {

    @InjectMocks
    private UserConfigController userConfigController;

    @Mock
    private UserConfigService userConfigService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userConfigController).build();
    }

    @DisplayName("사용자설정 수정")
    @Test
    public void 사용자설정_수정_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        UserConfig userConfig = UserConfigFactory.getUserConfig(1L);
        user.updateUserConfig(userConfig);

        doReturn(userConfig.getId()).when(userConfigService).updateUserConfig(anyLong(), any());
        UserConfigDto.UserConfigUpdateRequest requestDto = new UserConfigDto.UserConfigUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "fontSize", "16");

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/config")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestDto)));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<Long> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ApiResponse<Long>>() {});
        Long id = response.getResult();
        assertThat(id).isEqualTo(userConfig.getId());

        //verify
        verify(userConfigService, timeout(1)).updateUserConfig(anyLong(), any());
    }

    @DisplayName("사용자설정 조회")
    @Test
    public void 사용자설정_조회_Controller() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        UserConfigDto.UserConfigResponse responseDto = new UserConfigDto.UserConfigResponse(UserConfigFactory.getUserConfig(1L));
        doReturn(responseDto).when(userConfigService).findUserConfig(anyLong());

        //when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/config")
        .header("userId", user.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        ApiResponse<UserConfigDto.UserConfigResponse> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<UserConfigDto.UserConfigResponse>>() {});

        UserConfigDto.UserConfigResponse value = response.getResult();

        assertThat(value.getRecvAlaram()).isEqualTo(responseDto.getRecvAlaram());
        assertThat(value.getFontSize()).isEqualTo(responseDto.getFontSize());
        assertThat(value.getViewType()).isEqualTo(responseDto.getViewType());

        //verify
        verify(userConfigService, timeout(1)).findUserConfig(anyLong());
    }
    
}
