package com.wip.bool.web.controller.music;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.bible.WordsMasterRepository;
import com.wip.bool.domain.cmmn.dictionary.SearchStoreProxy;
import com.wip.bool.domain.music.*;
import com.wip.bool.web.dto.music.SongDetailDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "local")
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

    @Autowired
    private SearchStoreProxy searchStoreProxy;

    @Before
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
                .andExpect(jsonPath("$").isNumber())
                .andDo(print());

    }

    @Test
    public void 노래_추가_분류정보_누락() throws Exception {

        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();

        SongDetailDto.SongDetailSaveRequest requestDto = SongDetailDto.SongDetailSaveRequest.builder()
                .title(title)
                .lyrics(lyrics)
                .guitarCodeId(guitarCodes.get(0).getId())
                .wordsMasterId(wordsMasters.get(0).getId())
                .build();

        String url = "/api/v1/music/song-detail";

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void 노래_추가_기타코드정보_누락() throws Exception {

        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<SongMaster> songMasters = songMasterRepository.findAll();
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();

        SongDetailDto.SongDetailSaveRequest requestDto = SongDetailDto.SongDetailSaveRequest.builder()
                .title(title)
                .lyrics(lyrics)
                .codeId(songMasters.get(0).getId())
                .wordsMasterId(wordsMasters.get(0).getId())
                .build();

        String url = "/api/v1/music/song-detail";

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void 노래_추가_성경정보_누락() throws Exception {

        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<SongMaster> songMasters = songMasterRepository.findAll();
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();

        SongDetailDto.SongDetailSaveRequest requestDto = SongDetailDto.SongDetailSaveRequest.builder()
                .title(title)
                .lyrics(lyrics)
                .codeId(songMasters.get(0).getId())
                .guitarCodeId(guitarCodes.get(0).getId())
                .build();

        String url = "/api/v1/music/song-detail";

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void 노래_수정() throws Exception {


        String title = "우리에게 이 모든 선물을 주신 것은 입니다.";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<SongMaster> songMasters = songMasterRepository.findAll();
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();

        SongDetail songDetail = SongDetail.createSongDetail(title, lyrics, songMasters.get(0)
                , guitarCodes.get(0), wordsMasters.get(0));
        songDetailRepository.save(songDetail);

        SongDetailDto.SongDetailUpdateRequest requestDto = SongDetailDto.SongDetailUpdateRequest.builder()
                .title(title)
                .build();

        String url = "/api/v1/music/song-detail/" + songDetail.getId();

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .content(objectMapper.writeValueAsString(requestDto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(songDetail.getId()))
                .andDo(print());
    }

    @Test
    public void 노래_삭제() throws Exception {


        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<SongMaster> songMasters = songMasterRepository.findAll();
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();

        SongDetail songDetail = SongDetail.createSongDetail(title, lyrics, songMasters.get(0)
        , guitarCodes.get(0), wordsMasters.get(0));
        songDetailRepository.save(songDetail);

        String url = "/api/v1/music/song-detail/" + songDetail.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber())
                .andDo(print());
    }

    @Test
    public void 노래_조회() throws Exception {

        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<SongMaster> songMasters = songMasterRepository.findAll();
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();

        SongDetail songDetail = SongDetail.createSongDetail(title, lyrics, songMasters.get(0)
                , guitarCodes.get(0), wordsMasters.get(0));
        songDetailRepository.save(songDetail);

        String url = "/api/v1/music/song-details";
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("codeId", "0");
        param.add("order", "ASC");
        param.add("sort", "TITLE");
        param.add("size", "10");
        param.add("offset", "0");

        mockMvc.perform(MockMvcRequestBuilders.get(url)
        .params(param))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0]['id']").value(songDetail.getId()))
        .andExpect(jsonPath("$[0]['title']").value(songDetail.getTitle()))
        .andDo(print());

    }

    @Test
    public void 노래_검색() throws Exception {

        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<SongMaster> songMasters = songMasterRepository.findAll();
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();

        SongDetail songDetail = SongDetail.createSongDetail(title, lyrics, songMasters.get(0)
                , guitarCodes.get(0), wordsMasters.get(0));
        songDetailRepository.save(songDetail);
        searchStoreProxy.insert(title);

        String url = "/api/v1/music/song-details/search";

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("keyword", "우리에게");

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .params(param))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0]['title']").value(title))
                .andDo(print());
    }

    @Test
    public void 악보_추가() throws Exception {

    }

    @Test
    public void 악보_삭제() throws Exception {

    }

    @Test
    public void MP3파일_추가() throws Exception {

    }

    @Test
    public void MP3파일_삭제() throws Exception {

    }

}
