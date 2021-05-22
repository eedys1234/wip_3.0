package com.wip.bool.music.sheet.service;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.sheet.SongSheetFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.sheet.domain.SongSheet;
import com.wip.bool.music.sheet.domain.SongSheetRepository;
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
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongSheetServiceTest {

    @Value("${spring.images.path}")
    private String imagePath;

    @InjectMocks
    private SongSheetService songSheetService;

    @Mock
    private SongSheetRepository songSheetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongDetailRepository songDetailRepository;

    @DisplayName("악보 추가")
    @Test
    public void 악보_추가_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        List<SongSheet> songSheets = new ArrayList<>();
        SongSheet songSheet = SongSheetFactory.getSongSheet(songDetail, imagePath, SongSheetFactory.orgFileName, SongSheetFactory.byteString.getBytes(), 1, 1L);
        songSheets.add(songSheet);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songDetail)).when(songDetailRepository).findById(anyLong());
        doReturn(songSheets).when(songSheetRepository).findBySongDetail(anyLong());
        doReturn(songSheet).when(songSheetRepository).save(any(SongSheet.class));
        Long id = songSheetService.saveSongSheet(user.getId(), songDetail.getId(), SongSheetFactory.orgFileName, SongSheetFactory.byteString.getBytes());

        //then
        assertThat(id).isEqualTo(songSheet.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).findById(anyLong());
        verify(songSheetRepository, times(1)).findBySongDetail(anyLong());
        verify(songSheetRepository, times(1)).save(any(SongSheet.class));
    }

    @DisplayName("악보 삭제")
    @Test
    public void 악보_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);
        SongSheet songSheet = SongSheetFactory.getSongSheet(songDetail, imagePath, SongSheetFactory.orgFileName, SongSheetFactory.byteString.getBytes(), 1, 1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songSheet)).when(songSheetRepository).findById(anyLong());
        doReturn(1L).when(songSheetRepository).delete(any(SongSheet.class));
        Long resValue = songSheetService.deleteSongSheet(user.getId(), songSheet.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songSheetRepository, times(1)).findById(anyLong());
        verify(songSheetRepository, times(1)).delete(any(SongSheet.class));
    }
}
