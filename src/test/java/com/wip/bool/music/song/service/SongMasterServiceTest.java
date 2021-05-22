package com.wip.bool.music.song.service;

import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import com.wip.bool.music.song.dto.SongMasterDto;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongMasterServiceTest {

    @InjectMocks
    private SongMasterService songMasterService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongMasterRepository songMasterRepository;

    @DisplayName("SongMaster 추가")
    @Test
    public void SongMaster_추가_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        SongMasterDto.SongMasterSaveRequest requestDto = new SongMasterDto.SongMasterSaveRequest();
        ReflectionTestUtils.setField(requestDto, "codeName", SongMasterFactory.getCodeNames().get(0));

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(2L).when(songMasterRepository).findAllCount();
        doReturn(songMaster).when(songMasterRepository).save(any(SongMaster.class));
        Long id = songMasterService.saveSongMaster(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(songMaster.getId());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(songMasterRepository, times(1)).findAllCount();
        verify(songMasterRepository).save(any(SongMaster.class));
    }

    @DisplayName("SongMaster 삭제")
    @Test
    public void SongMaster_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(songMaster)).when(songMasterRepository).findById(any(Long.class));
        doReturn(1L).when(songMasterRepository).delete(any(SongMaster.class));
        Long resValue = songMasterService.deleteSongMaster(user.getId(), songMaster.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(songMasterRepository, times(1)).findById(any(Long.class));
        verify(songMasterRepository, times(1)).delete(any(SongMaster.class));
    }

    @DisplayName("SongMaster 리스트 조회")
    @Test
    public void SongMaster_리스트_조회_Service() throws Exception {

        //given
        List<SongMaster> songMasters = SongMasterFactory.getSongMastersWithId();

        //when
        doReturn(songMasters).when(songMasterRepository).findAll();
        List<SongMasterDto.SongMasterResponse> values = songMasterService.getsSongMaster();

        //then
        assertThat(values.size()).isEqualTo(songMasters.size());
        assertThat(values).extracting(SongMasterDto.SongMasterResponse::getId).containsAll(
                songMasters.stream()
                .map(SongMaster::getId)
                .collect(Collectors.toList()));

        assertThat(values).extracting(SongMasterDto.SongMasterResponse::getCodeKey).containsAll(
                songMasters.stream()
                        .map(SongMaster::getCodeKey)
                        .collect(Collectors.toList()));

        assertThat(values).extracting(SongMasterDto.SongMasterResponse::getCodeName).containsAll(
                songMasters.stream()
                        .map(SongMaster::getCodeName)
                        .collect(Collectors.toList()));

        //verify
        verify(songMasterRepository, times(1)).findAll();
    }
}
