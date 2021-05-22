package com.wip.bool.userboxsong.repository;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userbox.UserBoxFactory;
import com.wip.bool.cmmn.userboxsong.UserBoxSongFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.domain.UserBoxSong;
import com.wip.bool.userbox.domain.UserBoxSongRepository;
import com.wip.bool.userbox.dto.UserBoxSongDto;
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
public class UserBoxSongRepositoryTest {

    @Autowired
    private UserBoxSongRepository userBoxSongRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBoxRepository userBoxRepository;

    @Autowired
    private SongMasterRepository songMasterRepository;

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

    @Autowired
    private SongDetailRepository songDetailRepository;

    @BeforeEach
    public void user_init() throws Exception {
        User user = UserFactory.getNormalUser();
        userRepository.save(user);
    }

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

    private User findUser() {
        return userRepository.findAll().get(0);
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

    @DisplayName("사용자박스 곡 추가")
    @Test
    public void 사용자박스_곡_추가_Repository() throws Exception {

        //given
        User user = findUser();
        SongMaster songMaster = findSongMaster();
        GuitarCode guitarCode = findGuitarCode();
        WordsMaster wordsMaster = findWordsMaster();

        UserBox userBox = UserBoxFactory.getUserBox(user);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster);

        UserBox addUserBox = userBoxRepository.save(userBox);
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        UserBoxSong userBoxSong = UserBoxSongFactory.getUserBoxSong(addSongDetail, addUserBox);

        //when
        UserBoxSong addUserBoxSong = userBoxSongRepository.save(userBoxSong);

        //then
        List<UserBoxSong> userBoxSongs = userBoxSongRepository.findAll();
        assertThat(addUserBoxSong.getId()).isGreaterThan(0L);
        assertThat(addUserBoxSong.getId()).isEqualTo(userBoxSongs.get(0).getId());
        assertThat(addUserBoxSong.getUserBox().getId()).isEqualTo(userBox.getId());
        assertThat(addUserBoxSong.getSongDetail().getId()).isEqualTo(songDetail.getId());

    }

    @DisplayName("사용자박스 곡 삭제")
    @Test
    public void 사용자박스_곡_삭제_Repository() throws Exception {

        //given
        User user = findUser();
        SongMaster songMaster = findSongMaster();
        GuitarCode guitarCode = findGuitarCode();
        WordsMaster wordsMaster = findWordsMaster();

        UserBox userBox = UserBoxFactory.getUserBox(user);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster);

        UserBox addUserBox = userBoxRepository.save(userBox);
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        UserBoxSong userBoxSong = UserBoxSongFactory.getUserBoxSong(addSongDetail, addUserBox);
        UserBoxSong addUserBoxSong = userBoxSongRepository.save(userBoxSong);

        //when
        Long resValue = userBoxSongRepository.delete(addUserBoxSong);

        //then
        assertThat(resValue).isEqualTo(1L);

    }

    @DisplayName("사용자박스 곡 리스트 조회")
    @Test
    public void 사용자박스_곡_리스트_조회_Repository() throws Exception {

        //given
        SortType sortType = SortType.TITLE;
        OrderType orderType = OrderType.ASC;
        int size = 10;
        int offset = 0;

        User user = findUser();
        SongMaster songMaster = findSongMaster();
        GuitarCode guitarCode = findGuitarCode();
        WordsMaster wordsMaster = findWordsMaster();

        UserBox userBox = UserBoxFactory.getUserBox(user);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster);

        UserBox addUserBox = userBoxRepository.save(userBox);
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        UserBoxSong userBoxSong = UserBoxSongFactory.getUserBoxSong(addSongDetail, addUserBox);
        UserBoxSong addUserBoxSong = userBoxSongRepository.save(userBoxSong);

        //when
        List<UserBoxSongDto.UserBoxSongResponse> values = userBoxSongRepository.findAll(userBox.getId(), sortType, orderType, size, offset);

        //then
        assertThat(values.size()).isEqualTo(1);
        assertThat(values).extracting(UserBoxSongDto.UserBoxSongResponse::getSongDetailId).contains(songDetail.getId());
        assertThat(values).extracting(UserBoxSongDto.UserBoxSongResponse::getUserBoxSongId).contains(userBoxSong.getId());
    }

}
