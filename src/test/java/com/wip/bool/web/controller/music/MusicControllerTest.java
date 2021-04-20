package com.wip.bool.web.controller.music;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.bible.WordsMasterRepository;
import com.wip.bool.domain.music.*;
import com.wip.bool.web.dto.music.SongDetailDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
//@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MusicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SongDetailRepository songDetailRepository;

    @Autowired
    private SongMasterRepository songMasterRepository;

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

    @Autowired
    private ObjectMapper objectMapper;


    public void begin() throws Exception {

        //given
        SongMaster songMaster = SongMaster.createSongMaster("말씀의 노래", 1);
        GuitarCode guitarCode = GuitarCode.createGuitarCode("C", 1);
        WordsMaster wordsMaster = WordsMaster.createWordsMaster("창세기", 1);

        //when
        songMasterRepository.save(songMaster);
        guitarCodeRepository.save(guitarCode);
        wordsMasterRepository.save(wordsMaster);
    }

    @Test
    public void 노래_추가_테스트() throws Exception {

        begin();

        //given
        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<SongMaster> songMasters = songMasterRepository.findAll();
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();

        SongDetailDto.SongDetailSaveRequest requestDto = SongDetailDto.SongDetailSaveRequest.builder()
                .title(title)
                .lyrics(lyrics)
                .codeId(songMasters.get(0).getId())
                .guitarCodeId(guitarCodes.get(0).getId())
                .wordsMasterId(wordsMasters.get(0).getId())
                .build();

        String url = "/api/v1/music/song-detail";

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());


    }

}
