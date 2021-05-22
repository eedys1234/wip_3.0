package com.wip.bool.music.mp3.repository;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.mp3.SongMP3Factory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.mp3.domain.SongMP3;
import com.wip.bool.music.mp3.domain.SongMP3Repository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class SongMP3RepositoryTest {

    @Value("${spring.mp3.path}")
    private String mp3Path;

    @Autowired
    private SongMP3Repository songMP3Repository;

    @Autowired
    private SongDetailRepository songDetailRepository;

    @Autowired
    private SongMasterRepository songMasterRepository;

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

    @BeforeEach
    public void SongMaster_init() throws Exception {
        SongMaster songMaster = SongMasterFactory.getSongMaster("말씀의 노래", 1);
        songMasterRepository.save(songMaster);
    }

    @BeforeEach
    public void GuitarCode_init() throws Exception {
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode("C", 1);
        guitarCodeRepository.save(guitarCode);
    }

    @BeforeEach
    public void WordsMaster_init() throws Exception {
        WordsMaster wordsMaster = WordsMaster.createWordsMaster("창세기", 1);
        wordsMasterRepository.save(wordsMaster);
    }

    private SongMaster findSongMaster() {
        List<SongMaster> songMasters = songMasterRepository.findAll();
        return songMasters.get(0);
    }

    private GuitarCode findGuitarCode() {
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();
        return guitarCodes.get(0);
    }

    private WordsMaster findWordsMaster() {
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();
        return wordsMasters.get(0);
    }

    @DisplayName("mp3 파일 추가")
    @Test
    public void mp3_파일_추가_Repository() throws Exception {

        //given
        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        SongDetail addSongDetail = songDetailRepository.save(songDetail);
        SongMP3 songMP3 = SongMP3Factory.getSongMP3(addSongDetail, mp3Path, SongMP3Factory.orgFileName, SongMP3Factory.byteString.getBytes());

        //when
        SongMP3 addSongMP3 = songMP3Repository.save(songMP3);

        //then
        List<SongMP3> songMP3s = songMP3Repository.findAll();
        assertThat(addSongMP3.getId()).isGreaterThan(0L);
        assertThat(addSongMP3.getId()).isEqualTo(songMP3s.get(0).getId());
        assertThat(addSongMP3.getMp3OrgFileName()).isEqualTo(songMP3s.get(0).getMp3OrgFileName());
    }

    @DisplayName("mp3 파일 삭제")
    @Test
    public void mp3_파일_삭제_Repository() throws Exception {
        //given
        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        SongDetail addSongDetail = songDetailRepository.save(songDetail);
        SongMP3 songMP3 = SongMP3Factory.getSongMP3(addSongDetail, mp3Path, SongMP3Factory.orgFileName, SongMP3Factory.byteString.getBytes());

        SongMP3 addSongMP3 = songMP3Repository.save(songMP3);

        //when
        Long resValue = songMP3Repository.delete(addSongMP3);

        //then
        assertThat(resValue).isEqualTo(1L);
    }

    @DisplayName("mp3 파일 조회 by id")
    @Test
    public void mp3_파일_조회_byId_Repository() throws Exception {

        //given
        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        SongDetail addSongDetail = songDetailRepository.save(songDetail);
        SongMP3 songMP3 = SongMP3Factory.getSongMP3(addSongDetail, mp3Path, SongMP3Factory.orgFileName, SongMP3Factory.byteString.getBytes());

        SongMP3 addSongMP3 = songMP3Repository.save(songMP3);

        //when
        SongMP3 value = songMP3Repository.findById(addSongMP3.getId())
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_MP3));

        //then
        assertThat(value.getId()).isEqualTo(addSongMP3.getId());
        assertThat(value.getMp3OrgFileName()).isEqualTo(addSongMP3.getMp3OrgFileName());
    }

    @DisplayName("mp3 파일 조회 by SongDetail")
    @Test
    public void mp3_파일_조회_bySongDetail_Repository() throws Exception {

        //given
        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        SongDetail addSongDetail = songDetailRepository.save(songDetail);
        SongMP3 songMP3 = SongMP3Factory.getSongMP3(addSongDetail, mp3Path, SongMP3Factory.orgFileName, SongMP3Factory.byteString.getBytes());

        SongMP3 addSongMP3 = songMP3Repository.save(songMP3);

        //when
        SongMP3 value = songMP3Repository.findBySongDetail(addSongDetail.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_MP3));

        //then
        assertThat(value.getId()).isEqualTo(addSongMP3.getId());
        assertThat(value.getMp3OrgFileName()).isEqualTo(addSongMP3.getMp3OrgFileName());

    }
}
