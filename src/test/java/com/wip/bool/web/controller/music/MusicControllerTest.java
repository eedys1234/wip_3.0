package com.wip.bool.web.controller.music;

import com.wip.bool.domain.bible.WordsMaster;
import com.wip.bool.domain.bible.WordsMasterRepository;
import com.wip.bool.domain.music.*;
import com.wip.bool.web.dto.music.SongDetailDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
public class MusicControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SongDetailRepository songDetailRepository;

    @Autowired
    private SongMasterRepository songMasterRepository;

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

    @LocalServerPort
    private int port;

    @Before
    @Transactional
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
    @Transactional
    public void 노래_추가_테스트() throws Exception {

        //given
        String title = "우리에게 이 모든 선물을 주신 것은";
        String lyrics = "에베소서 4:11-16 (14절 제외, 쉬운 ver)";

        List<SongMaster> songMasters = songMasterRepository.findAll();
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();

        SongDetailDto.SongDetailSaveRequest requestDto = SongDetailDto.SongDetailSaveRequest.builder()
                .title(title)
                .lyrics(lyrics)
                .codeKey(songMasters.get(0).getId())
                .guitarCodeKey(guitarCodes.get(0).getId())
                .wordsMasterKey(wordsMasters.get(0).getId())
                .build();

        String url = "http://localhost:" + port + "/api/v1/music/song-detail";

        //when
        ResponseEntity<Long> entity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(entity.getBody()).isGreaterThan(0L);

        Long id = entity.getBody();

        SongDetail songDetail = songDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(""));

        assertThat(songDetail.getTitle()).isEqualTo(title);
        assertThat(songDetail.getLyrics()).isEqualTo(lyrics);

    }

}
