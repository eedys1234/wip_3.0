package com.wip.bool.music.song.service;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.dictionary.SearchStoreProxy;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.mp3.SongMP3Factory;
import com.wip.bool.cmmn.music.sheet.SongSheetFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.mp3.domain.SongMP3;
import com.wip.bool.music.mp3.domain.SongMP3Repository;
import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.sheet.domain.SongSheetRepository;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import com.wip.bool.music.song.dto.SongDetailDto;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongDetailServiceTest {

    @Value("${spring.images.path}")
    private String sheetFilePath;

    @Value("${spring.mp3.path}")
    private String mp3FilePath;

    @InjectMocks
    private SongDetailService songDetailService;

    @Mock
    private SongDetailRepository songDetailRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GuitarCodeRepository guitarCodeRepository;

    @Mock
    private WordsMasterRepository wordsMasterRepository;

    @Mock
    private SongMasterRepository songMasterRepository;

    @Mock
    private SongSheetRepository songSheetRepository;

    @Mock
    private SongMP3Repository songMP3Repository;

    @Mock
    private SearchStoreProxy searchStore;

    @DisplayName("곡 추가")
    @Test
    public void 곡_추가_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster );

        SongDetailDto.SongDetailSaveRequest requestDto = new SongDetailDto.SongDetailSaveRequest();
        ReflectionTestUtils.setField(requestDto, "title", songDetail.getTitle());
        ReflectionTestUtils.setField(requestDto, "lyrics", songDetail.getLyrics());
        ReflectionTestUtils.setField(requestDto, "codeId", songMaster.getId());
        ReflectionTestUtils.setField(requestDto, "guitarCodeId", guitarCode.getId());
        ReflectionTestUtils.setField(requestDto, "wordsMasterId", wordsMaster.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songMaster)).when(songMasterRepository).findById(anyLong());
        doReturn(Optional.ofNullable(guitarCode)).when(guitarCodeRepository).findById(anyLong());
        doReturn(Optional.ofNullable(wordsMaster)).when(wordsMasterRepository).findById(anyLong());
        doReturn(songDetail).when(songDetailRepository).save(any(SongDetail.class));
        when(searchStore.insert(anyString())).thenReturn(true);

        Long id = songDetailService.saveSong(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(songDetail.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songMasterRepository, times(1)).findById(anyLong());
        verify(guitarCodeRepository, times(1)).findById(anyLong());
        verify(wordsMasterRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).save(any(SongDetail.class));
        verify(searchStore, times(1)).insert(anyString());
    }


    @DisplayName("곡 수정")
    @Test
    public void 곡_수정_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        SongDetailDto.SongDetailUpdateRequest requestDto = new SongDetailDto.SongDetailUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "title", songDetail.getTitle());
        ReflectionTestUtils.setField(requestDto, "lyrics", songDetail.getLyrics());
        ReflectionTestUtils.setField(requestDto, "codeId", songMaster.getId());
        ReflectionTestUtils.setField(requestDto, "guitarCodeId", guitarCode.getId());
        ReflectionTestUtils.setField(requestDto, "wordsMasterId", wordsMaster.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songDetail)).when(songDetailRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songMaster)).when(songMasterRepository).findById(anyLong());
        doReturn(Optional.ofNullable(guitarCode)).when(guitarCodeRepository).findById(anyLong());
        doReturn(Optional.ofNullable(wordsMaster)).when(wordsMasterRepository).findById(anyLong());
        when(searchStore.delete(anyString())).thenReturn(true);
        when(searchStore.insert(anyString())).thenReturn(true);
        Long id = songDetailService.updateSong(user.getId(), songDetail.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(songDetail.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).findById(anyLong());
        verify(songMasterRepository, times(1)).findById(anyLong());
        verify(guitarCodeRepository, times(1)).findById(anyLong());
        verify(wordsMasterRepository, times(1)).findById(anyLong());
        verify(searchStore, times(1)).delete(anyString());
        verify(searchStore, times(1)).insert(anyString());
    }

    @DisplayName("곡 삭제")
    @Test
    public void 곡_삭제_Service() throws Exception {

        //given
        String byteString = "TEST";
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        List<SongSheet> songSheets = SongSheetFactory.getSongSheets(songDetail, sheetFilePath, "test.PNG", byteString.getBytes());
        SongMP3 songMP3 = SongMP3Factory.getSongMP3(songDetail, mp3FilePath, "test.MP4", byteString.getBytes(), 1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songDetail)).when(songDetailRepository).findById(anyLong());
        doReturn(1L).when(songDetailRepository).delete(any(SongDetail.class));
        doReturn(songSheets).when(songSheetRepository).findBySongDetail(anyLong());
        doReturn(songMP3).when(songMP3Repository).findBySongDetail(any(SongDetail.class));
        Long resValue = songDetailService.deleteSong(user.getId(), songDetail.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).delete(any(SongDetail.class));
        verify(songSheetRepository, times(1)).findBySongDetail(anyLong());
        verify(songMP3Repository, times(1)).findBySongDetail(any(SongDetail.class));
    }

    @DisplayName("곡 리스트 간단 조회")
    @Test
    public void 곡_리스트_간단_조회_Service() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        String sort = "TITLE";
        String order = "ASC";
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);

        List<SongDetail> songDetails = SongDetailFactory.getSongDetailsWithId(songMaster, guitarCode, wordsMaster);

        //when
        doReturn(Optional.ofNullable(songMaster)).when(songMasterRepository).findById(anyLong());
        doReturn(songDetails.stream()
                .map(songDetail -> new SongDetailDto.SongDetailSimpleResponse(songDetail.getId(), songDetail.getTitle(), songDetail.getGuitarCode().getId()))
                .collect(Collectors.toList()))
                .when(songDetailRepository).findAll(any(SongMaster.class), any(SortType.class), any(OrderType.class), anyInt(), anyInt());

        List<SongDetailDto.SongDetailSimpleResponse> values = songDetailService.findAll(songMaster.getId(), order, sort, size, offset);

        //then
        assertThat(values.size()).isEqualTo(songDetails.size());
        assertThat(values).extracting(SongDetailDto.SongDetailSimpleResponse::getTitle)
                .containsAll(SongDetailFactory.getSongDetailTitle());

        assertThat(values).extracting(SongDetailDto.SongDetailSimpleResponse::getId)
                .containsAll(songDetails.stream().map(SongDetail::getId).collect(Collectors.toList()));

        //verify
        verify(songMasterRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).findAll(any(SongMaster.class), any(SortType.class), any(OrderType.class), anyInt(), anyInt());
    }

    @DisplayName("곡 상세조회")
    @Test
    public void 곡_상세조회_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        //when
        doReturn(Optional.ofNullable(new SongDetailDto.SongDetailResponse(songDetail))).when(songDetailRepository).findById(anyLong(), anyLong());
        SongDetailDto.SongDetailResponse value = songDetailService.findDetailOne(songDetail.getId(), user.getId());

        //then
        assertThat(value.getId()).isEqualTo(songDetail.getId());
        assertThat(value.getCodeId()).isEqualTo(songDetail.getSongMaster().getId());
        assertThat(value.getGuitarCodeId()).isEqualTo(songDetail.getGuitarCode().getId());

        //verify
        verify(songDetailRepository, times(1)).findById(anyLong(), anyLong());
    }

    @DisplayName("곡 검색")
    @Test
    public void 곡_검색_Service() throws Exception {

        //given
        String keyword = "";
        List<String> songTitles = SongDetailFactory.getSongDetailTitle();

        //when
        when(searchStore.findWords(anyString())).thenReturn(songTitles);
        List<SongDetailDto.SongDetailSimpleResponse> values = songDetailService.search(keyword);

        //then
        assertThat(values.size()).isEqualTo(songTitles.size());
        assertThat(values).extracting(SongDetailDto.SongDetailSimpleResponse::getTitle).containsAll(songTitles);

        //verify
        verify(searchStore, times(1)).findWords(anyString());
    }

}
