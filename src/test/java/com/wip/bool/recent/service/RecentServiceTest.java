package com.wip.bool.recent.service;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.recent.RecentFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.recent.domain.Recent;
import com.wip.bool.recent.domain.RecentRepository;
import com.wip.bool.recent.dto.RecentDto;
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
public class RecentServiceTest {

    @InjectMocks
    private RecentService recentService;

    @Mock
    private RecentRepository recentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongDetailRepository songDetailRepository;

    @DisplayName("최근들은내역 추가")
    @Test
    public void 최근들은내역_추가_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        Recent recent = RecentFactory.getRecent(user, songDetail, 1L);

        RecentDto.RecentSaveRequest requestDto = new RecentDto.RecentSaveRequest();
        ReflectionTestUtils.setField(requestDto, "songDetailId", songDetail.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songDetail)).when(songDetailRepository).findById(anyLong());
        doReturn(recent).when(recentRepository).save(any(Recent.class));
        Long id = recentService.save(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(recent.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).findById(anyLong());
        verify(recentRepository, times(1)).save(any(Recent.class));
    }
    
    @DisplayName("최근들은내역 조회")
    @Test
    public void 최근들은내역_조회_Service() throws Exception {

        //given
        int size = 10;
        int offset = 0;
        User user = UserFactory.getNormalUser(1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);

        List<SongDetail> songDetails = SongDetailFactory.getSongDetailsWithId(songMaster, guitarCode, wordsMaster);
        List<Recent> recents = RecentFactory.getRcents(user, songDetails);

        //when
        doReturn(recents.stream()
                .map(recent -> new RecentDto.RecentResponse(recent.getId(), recent.getSongDetail().getId(), recent.getSongDetail().getTitle(), recent.getCreateDate()))
                .collect(Collectors.toList())).when(recentRepository).findAll(anyLong(), anyInt(), anyInt());

        List<RecentDto.RecentResponse> values = recentService.gets(user.getId(), size, offset);

        //then
        assertThat(values.size()).isEqualTo(recents.size());
        assertThat(values).extracting(RecentDto.RecentResponse::getSongDetailId)
                .containsAll(songDetails.stream().map(SongDetail::getId).collect(Collectors.toList()));

        //verify
        verify(recentRepository, times(1)).findAll(anyLong(), anyInt(), anyInt());
    }
}
