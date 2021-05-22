package com.wip.bool.music.sheet.repository;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.sheet.SongSheetFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.sheet.domain.SongSheetRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SongSheetRepositoryTest {

    @Value("${spring.images.path}")
    private String imagePath;

    @Autowired
    private SongMasterRepository songMasterRepository;

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

    @Autowired
    private SongDetailRepository songDetailRepository;

    @Autowired
    private SongSheetRepository songSheetRepository;

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

    private SongDetail findSongDetail() {
        List<SongDetail> songDetails = songDetailRepository.findAll();
        return songDetails.get(0);
    }

    @DisplayName("악보 추가")
    @Test
    public void 악보_추가_Repository() throws Exception {

        //given
        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        songDetailRepository.save(songDetail);

        SongSheet songSheet = SongSheetFactory.getSongSheet(songDetail, imagePath, SongSheetFactory.orgFileName, SongSheetFactory.byteString.getBytes(), 1);

        //when
        SongSheet addSongSheet = songSheetRepository.save(songSheet);

        //then
        List<SongSheet> songSheets = songSheetRepository.findBySongDetail(songDetail.getId());
        assertThat(addSongSheet.getId()).isEqualTo(songSheets.get(0).getId());
        assertThat(addSongSheet.getSheetFilePath()).isEqualTo(songSheets.get(0).getSheetFilePath());
        assertThat(addSongSheet.getSheetFileExt()).isEqualTo(songSheets.get(0).getSheetFileExt());
        assertThat(addSongSheet.getSheetOrder()).isEqualTo(songSheets.get(0).getSheetOrder());
    }

    @DisplayName("악보 삭제")
    @Test
    public void 악보_삭제_Repository() throws Exception {

        //given
        String orgFileName = "TEST.PNG";
        String byteString = "TEST";
        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        songDetailRepository.save(songDetail);

        SongSheet songSheet = SongSheetFactory.getSongSheet(songDetail, imagePath, orgFileName, byteString.getBytes(), 1);
        SongSheet addSongSheet = songSheetRepository.save(songSheet);

        //when
        Long resValue = songSheetRepository.delete(addSongSheet);

        //then
        assertThat(resValue).isEqualTo(1L);
    }

    @DisplayName("악보 조회 by Id")
    @Test
    public void 악보_조회_byId_Repository() throws Exception {

        //given
        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        songDetailRepository.save(songDetail);

        SongSheet songSheet = SongSheetFactory.getSongSheet(songDetail, imagePath, SongSheetFactory.orgFileName, SongSheetFactory.byteString.getBytes(), 1);
        SongSheet addSongSheet = songSheetRepository.save(songSheet);

        //when
        SongSheet value = songSheetRepository.findById(addSongSheet.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_SHEET));

        //then
        assertThat(value.getId()).isGreaterThan(0L);
        assertThat(value.getSongDetail().getId()).isEqualTo(songDetail.getId());
        assertThat(value.getSheetFilePath()).isEqualTo(imagePath);
        assertThat(value.getSheetOrder()).isEqualTo(1);
        assertThat(value.getSheetOrgFileName()).isEqualTo(SongSheetFactory.orgFileName);
    }

    @DisplayName("악보 조회 by SongDetail")
    @Test
    public void 악보_조회_bySongDetail_Repository() throws Exception {

        //given
        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        songDetailRepository.save(songDetail);

        SongSheet songSheet = SongSheetFactory.getSongSheet(songDetail, imagePath, SongSheetFactory.orgFileName, SongSheetFactory.byteString.getBytes(), 1);
        SongSheet addSongSheet = songSheetRepository.save(songSheet);

        //when
        List<SongSheet> values = songSheetRepository.findBySongDetail(songDetail.getId());

        //then
        assertThat(values.size()).isEqualTo(1);
        assertThat(values.get(0).getSongDetail().getId()).isEqualTo(songDetail.getId());
        assertThat(values.get(0).getSheetOrgFileName()).isEqualTo(SongSheetFactory.orgFileName);
        assertThat(values.get(0).getSheetOrder()).isEqualTo(1);
    }
}
