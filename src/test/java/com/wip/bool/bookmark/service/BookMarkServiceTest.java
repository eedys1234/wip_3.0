package com.wip.bool.bookmark.service;

import com.wip.bool.bible.domain.WordsMaster;
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
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookMarkServiceTest {

    @InjectMocks
    private BookMarkService bookMarkService;

    @Mock
    private BookMarkRepository bookMarkRepository;
    
    @Mock
    private SongDetailRepository songDetailRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @DisplayName("즐겨찾기 추가")
    @Test
    public void 즐겨찾기_추가_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        BookMark bookMark = BookMarkFactory.getBookMark(user, songDetail, 1L);

        BookMarkDto.BookMarkSaveRequest requestDto = new BookMarkDto.BookMarkSaveRequest();
        ReflectionTestUtils.setField(requestDto, "songDetailId", songDetail.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songDetail)).when(songDetailRepository).findById(anyLong());
        doReturn(bookMark).when(bookMarkRepository).save(any(BookMark.class));
        Long id = bookMarkService.saveBookMark(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(bookMark.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).findById(anyLong());
        verify(bookMarkRepository, times(1)).save(any(BookMark.class));
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    public void 즐겨찾기_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        BookMark bookMark = BookMarkFactory.getBookMark(user, songDetail, 1L);

        //when
        doReturn(Optional.ofNullable(bookMark)).when(bookMarkRepository).findById(anyLong(), anyLong());
        doReturn(1L).when(bookMarkRepository).delete(any(BookMark.class));
        Long resValue = bookMarkService.deleteBookMark(user.getId(), bookMark.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(bookMarkRepository, times(1)).findById(anyLong(), anyLong());
        verify(bookMarkRepository, times(1)).delete(any(BookMark.class));
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    public void 즐겨찾기_조회_Service() throws Exception {

        //given
        String sort = "TITLE";
        String order = "ASC";
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        List<SongDetail> songDetails = SongDetailFactory.getSongDetailsWithId(songMaster, guitarCode, wordsMaster);
        List<BookMark> bookMarks = BookMarkFactory.getBookMarksWithId(user, songDetails);

        //when
        doReturn(bookMarks.stream().map(bookMark -> new BookMarkDto.BookMarkResponse(bookMark.getId(), bookMark.getSongDetail().getId(),
                bookMark.getSongDetail().getTitle(), bookMark.getSongDetail().getGuitarCode().getCode(), bookMark.getCreateDate()))
                .collect(Collectors.toList()))
                .when(bookMarkRepository).findAll(anyLong(), any(SortType.class), any(OrderType.class), anyInt(), anyInt());

        List<BookMarkDto.BookMarkResponse> values = bookMarkService.findBookMarks(user.getId(), sort, order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(bookMarks.size());
        assertThat(values).extracting(BookMarkDto.BookMarkResponse::getSongDetailId)
                .containsAll(songDetails.stream().map(SongDetail::getId).collect(Collectors.toList()));

        //verify
        verify(bookMarkRepository, times(1)).findAll(anyLong(), any(SortType.class), any(OrderType.class), anyInt(), anyInt());
    }
}
