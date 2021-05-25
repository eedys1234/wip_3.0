package com.wip.bool.bookmark.repository;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.bookmark.domain.BookMark;
import com.wip.bool.bookmark.domain.BookMarkRepository;
import com.wip.bool.bookmark.dto.BookMarkDto;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.bookmark.BookMarkFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class BookMarkRepositoryTest {

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongDetailRepository songDetailRepository;

    @Autowired
    private SongMasterRepository songMasterRepository;

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

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

    @DisplayName("즐겨찾기 추가")
    @Test
    public void 즐겨찾기_추가_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        BookMark bookMark = BookMarkFactory.getBookMark(user, songDetail);

        //when
        BookMark addBookMark = bookMarkRepository.save(bookMark);

        //then
        List<BookMark> bookMarks = bookMarkRepository.findAll();
        assertThat(addBookMark.getId()).isGreaterThan(0L);
        assertThat(addBookMark.getId()).isEqualTo(bookMarks.get(0).getId());
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    public void 즐겨찾기_삭제_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        SongDetail songDetail = SongDetailFactory.getSongDetail(findSongMaster(), findGuitarCode(), findWordsMaster());
        SongDetail addSongDetail = songDetailRepository.save(songDetail);

        BookMark bookMark = BookMarkFactory.getBookMark(user, songDetail);
        BookMark addBookMark = bookMarkRepository.save(bookMark);

        //when
        Long resValue = bookMarkRepository.delete(addBookMark);

        //then
        assertThat(resValue).isEqualTo(1L);
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    public void 즐겨찾기_조회_Repository() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);

        List<SongDetail> songDetails = SongDetailFactory.getSongDetails(findSongMaster(), findGuitarCode(), findWordsMaster());

        for(SongDetail songDetail : songDetails)
        {
            songDetailRepository.save(songDetail);
        }

        List<BookMark> bookMarks = BookMarkFactory.getBookMarks(user, songDetails);

        for(BookMark bookMark : bookMarks)
        {
            bookMarkRepository.save(bookMark);
        }

        //when
        List<BookMarkDto.BookMarkResponse> values = bookMarkRepository.findAll(user.getId(), SortType.TITLE, OrderType.ASC, size, offset);

        //then
        assertThat(values.size()).isEqualTo(bookMarks.size());
        assertThat(values).extracting(BookMarkDto.BookMarkResponse::getSongDetailId)
                .containsAll(songDetails.stream().map(songDetail -> songDetail.getId()).collect(Collectors.toList()));

    }
}
