package com.wip.bool.music.mp3.service;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.mp3.SongMP3Factory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.mp3.domain.SongMP3;
import com.wip.bool.music.mp3.domain.SongMP3Repository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongMP3ServiceTest {

    @Value("${spring.mp3.path}")
    private String mp3Path;

    @InjectMocks
    private SongMP3Service songMP3Service;

    @Mock
    private SongMP3Repository songMP3Repository;

    @Mock
    private SongDetailRepository songDetailRepository;

    @Mock
    private UserRepository userRepository;


    @DisplayName("mp3 파일 추가")
    @Test
    public void mp3_파일_추가_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        SongMP3 songMP3 = SongMP3Factory.getSongMP3(songDetail, mp3Path, SongMP3Factory.orgFileName, SongMP3Factory.byteString.getBytes(), 1L);
        byte[] mp3FileBytes = Files.readAllBytes(FileSystems.getDefault().getPath("src/test/resources/mp3", SongMP3Factory.orgFileName));

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songDetail)).when(songDetailRepository).findById(anyLong());
        doReturn(songMP3).when(songMP3Repository).save(any(SongMP3.class));

        Long id = songMP3Service.saveSongMP3(user.getId(), songDetail.getId(), SongMP3Factory.orgFileName, mp3FileBytes);

        //then
        assertThat(id).isEqualTo(songMP3.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).findById(anyLong());
        verify(songMP3Repository, times(1)).save(any(SongMP3.class));
    }

    @DisplayName("mp3 파일 삭제")
    @Test
    public void mp3_파일_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        SongMP3 songMP3 = SongMP3Factory.getSongMP3(songDetail, mp3Path, SongMP3Factory.orgFileName, SongMP3Factory.byteString.getBytes(), 1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songMP3)).when(songMP3Repository).findById(anyLong());
        doReturn(1L).when(songMP3Repository).delete(any(SongMP3.class));
        Long resValue = songMP3Service.deleteSongMP3(user.getId(), songMP3.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songMP3Repository, times(1)).findById(anyLong());
        verify(songMP3Repository, times(1)).delete(any(SongMP3.class));
    }

    @DisplayName("mp3 파일 가져오기")
    @Test
    public void mp3_파일_가져오기_Service() throws Exception {

        //given
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        byte[] mp3FileBytes = Files.readAllBytes(FileSystems.getDefault().getPath("src/test/resources/mp3", SongMP3Factory.orgFileName));
        SongMP3 songMP3 = SongMP3Factory.getSongMP3(songDetail, "src/test/resources/mp3", SongMP3Factory.orgFileName, mp3FileBytes, 1L);
        ReflectionTestUtils.setField(songMP3, "mp3NewFileName", "0B5B8DF97B594F69B8F50CE7136CF9E1");
        //when
        doReturn(Optional.ofNullable(songMP3)).when(songMP3Repository).findById(anyLong());
        byte[] bytes = songMP3Service.getFile(songMP3.getId());

        //then
        assertThat(bytes).isNotNull();
        assertThat(bytes).isEqualTo(mp3FileBytes);
        assertThat(bytes.length).isGreaterThan(0);

        //verify
        verify(songMP3Repository, times(1)).findById(anyLong());
    }

}
