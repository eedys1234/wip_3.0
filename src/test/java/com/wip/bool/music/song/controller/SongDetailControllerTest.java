package com.wip.bool.music.song.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.music.song.service.SongDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class SongDetailControllerTest {

    @InjectMocks
    private SongDetailController songDetailController;

    @Mock
    private SongDetailService songDetailService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(songDetailController).build();
        objectMapper = new ObjectMapper();
    }



}
