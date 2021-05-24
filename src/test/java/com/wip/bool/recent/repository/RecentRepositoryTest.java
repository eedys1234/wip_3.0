package com.wip.bool.recent.repository;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.recent.RecentFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import com.wip.bool.recent.domain.Recent;
import com.wip.bool.recent.domain.RecentRepository;
import com.wip.bool.recent.dto.RecentDto;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class RecentRepositoryTest {


    @Autowired
    private RecentRepository recentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongMasterRepository songMasterRepository;

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

    @Autowired
    private SongDetailRepository songDetailRepository;

    @BeforeEach
    public void songMaster_init() throws Exception {
        SongMaster songMaster = SongMasterFactory.getSongMaster();
        songMasterRepository.save(songMaster);
    }

    @BeforeEach
    public void guitarCode_init() throws Exception {
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode();
        guitarCodeRepository.save(guitarCode);
    }

    @BeforeEach
    public void wordsMaster_init() throws Exception {
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster();
        wordsMasterRepository.save(wordsMaster);
    }

    private SongMaster findSongMaster() {
        return songMasterRepository.findAll().get(0);
    }

    private GuitarCode findGuitarCode() {
        return guitarCodeRepository.findAll().get(0);
    }

    private WordsMaster findWordsMaster() {
        return wordsMasterRepository.findAll().get(0);
    }


    @DisplayName("최근들은내역 추가")
    @Test
    public void 최근들은내역_추가_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        Recent recent = RecentFactory.getRecent(addUser, addSongDetail);

        //when
        Recent addRecent = recentRepository.save(recent);

        //then
        assertThat(addRecent.getId()).isGreaterThan(0L);
    }

    @DisplayName("최근들은내역 조회")
    @Test
    public void 최근들은내역_조회_Repository() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        List<SongDetail> songDetails = SongDetailFactory.getSongDetails(findSongMaster(), findGuitarCode(), findWordsMaster());
        List<Recent> recents = RecentFactory.getRcents(user, songDetails);

        for(SongDetail songDetail : songDetails)
        {
            songDetailRepository.save(songDetail);
        }

        for(Recent recent : recents)
        {
            recentRepository.save(recent);
        }

        //when
        List<RecentDto.RecentResponse> values = recentRepository.findAll(user.getId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(recents.size());
        assertThat(values).extracting(RecentDto.RecentResponse::getSongDetailId)
                .containsAll(songDetails.stream().map(SongDetail::getId).collect(Collectors.toList()));
    }
}
