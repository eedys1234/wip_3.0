package com.wip.bool.web.controller.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.app.domain.AppVersion;
import com.wip.bool.app.domain.AppVersionRepository;
import com.wip.bool.app.dto.AppVersionDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "local")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppVersionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Before
    public void init() throws Exception {

        String name = "ILLECTORNC";
        String version = "1.0.0.0";

        AppVersion appVersion = AppVersion.createAppVersion(name, version);
        appVersionRepository.save(appVersion);
    }

    @Test
    public void app_정보_추가() throws Exception {

        String name = "ILLECTORN";
        String version = "1.0.0.0";

        AppVersionDto.AppVersionSaveRequest reqeustDto = new AppVersionDto.AppVersionSaveRequest(name, version);

        String url = "/api/v1/app/version";

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(objectMapper.writeValueAsString(reqeustDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNumber())
                .andDo(print());
    }

    @Test
    public void app_정보_리스트_조회() throws Exception {

        String url = "/api/v1/app/versions";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isMap())
                .andDo(print());
    }

    @Test
    public void app_정보_조회() throws Exception {

        String name = "ILLECTORNC";

        String url = "/api/v1/app/version";

        mockMvc.perform(MockMvcRequestBuilders.get(url)
               .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andDo(print());

    }
 }
