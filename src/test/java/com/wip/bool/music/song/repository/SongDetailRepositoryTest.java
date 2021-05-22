package com.wip.bool.music.song.repository;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import com.wip.bool.music.song.dto.SongDetailDto;
import com.wip.bool.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SongDetailRepositoryTest {

    @Autowired
    private SongMasterRepository songMasterRepository;

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

    @Autowired
    private SongDetailRepository songDetailRepository;

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

    @DisplayName("곡 추가")
    @Test
    public void 곡_추가_Repository() throws Exception {

        //given
        SongMaster songMaster = findSongMaster();
        GuitarCode guitarCode = findGuitarCode();
        WordsMaster wordsMaster = findWordsMaster();
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster);

        //when
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        //then
        List<SongDetail> songDetails = songDetailRepository.findAll();

        assertThat(addSongDetail.getId()).isGreaterThan(0L);
        assertThat(addSongDetail.getId()).isEqualTo(songDetails.get(0).getId());
        assertThat(addSongDetail.getTitle()).isEqualTo(songDetails.get(0).getTitle());
        assertThat(addSongDetail.getLyrics()).isEqualTo(songDetails.get(0).getLyrics());
    }

    @DisplayName("곡 삭제")
    @Test
    public void 곡_삭제_Repository() throws Exception {

        //given
        SongMaster songMaster = findSongMaster();
        GuitarCode guitarCode = findGuitarCode();
        WordsMaster wordsMaster = findWordsMaster();
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster);
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        //when
        Long resValue = songDetailRepository.delete(addSongDetail);

        //then
        List<SongDetail> songDetails = songDetailRepository.findAll();
        assertThat(resValue).isEqualTo(1L);
        assertThat(songDetails.size()).isEqualTo(0);
    }

    @DisplayName("곡 상세정보 조회")
    @Test
    public void 곡_상세정보_조회_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = findSongMaster();
        GuitarCode guitarCode = findGuitarCode();
        WordsMaster wordsMaster = findWordsMaster();
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster);
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        //when
        SongDetailDto.SongDetailResponse value = songDetailRepository.findById(addSongDetail.getId(), user.getId())
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.NOT_FOUND_SONG));

        //then
        assertThat(value.getId()).isEqualTo(addSongDetail.getId());
        assertThat(value.getTitle()).isEqualTo(addSongDetail.getTitle());
        assertThat(value.getCodeId()).isEqualTo(songMaster.getId());
        assertThat(value.getGuitarCodeId()).isEqualTo(guitarCode.getId());
        assertThat(value.getGuitarCode()).isEqualTo(guitarCode.getCode());
        assertThat(value.getBookmarkId()).isNull();

    }

    @DisplayName("곡 리스트(간단정보) 조회")
    @Test
    public void 곡_리스트_간단정보_조회_Repository() throws Exception {

        //given
        SortType sortType = SortType.TITLE;
        OrderType orderType = OrderType.ASC;
        int size = 10;
        int offset = 0;

        SongMaster songMaster = findSongMaster();
        GuitarCode guitarCode = findGuitarCode();
        WordsMaster wordsMaster = findWordsMaster();
        List<SongDetail> songDetails = SongDetailFactory.getSongDetails(songMaster, guitarCode, wordsMaster);

        for(SongDetail songDetail : songDetails)
        {
            songDetailRepository.save(songDetail);
        }

        //when
        List<SongDetailDto.SongDetailSimpleResponse> values = songDetailRepository.findAll(songMaster, sortType, orderType, size, offset);

        //then
        assertThat(values.size()).isEqualTo(songDetails.size());
        assertThat(values).extracting(SongDetailDto.SongDetailSimpleResponse::getTitle).containsAll(SongDetailFactory.getSongDetailTitle());
    }
}
