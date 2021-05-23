package com.wip.bool.userboxsong.service;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.dept.DeptFactory;
import com.wip.bool.cmmn.group.GroupFactory;
import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.music.song.SongDetailFactory;
import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.cmmn.rights.RightsFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.SortType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userbox.UserBoxFactory;
import com.wip.bool.cmmn.userboxsong.UserBoxSongFactory;
import com.wip.bool.group.domain.Group;
import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.song.domain.SongDetail;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepositoryImpl;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.domain.UserBoxSong;
import com.wip.bool.userbox.domain.UserBoxSongRepository;
import com.wip.bool.userbox.dto.UserBoxSongDto;
import com.wip.bool.userbox.service.UserBoxSongService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserBoxSongServiceTest {

    @InjectMocks
    private UserBoxSongService userBoxSongService;

    @Mock
    private UserBoxSongRepository userBoxSongRepository;

    @Mock
    private SongDetailRepository songDetailRepository;

    @Mock
    private UserBoxRepository userBoxRepository;

    @Mock
    private RightsRepositoryImpl rightsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @DisplayName("사용자박스 곡 추가")
    @Test
    public void 사용자박스_곡_추가_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(DeptFactory.getDept(), 1L);
        List<Group> groups = GroupFactory.getGroupsWithId(user);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        UserBoxSong userBoxSong = UserBoxSongFactory.getUserBoxSong(songDetail, userBox, 1L);
        List<Rights> rights = new ArrayList<>();
        String rightType = "read,write";
        rights.add(RightsFactory.getUserBoxRightsWithUser(userBox.getId(), user.getId(), rightType, 1L));

        UserBoxSongDto.UserBoxSongSaveRequest requestDto = new UserBoxSongDto.UserBoxSongSaveRequest();
        ReflectionTestUtils.setField(requestDto, "songDetailId", songDetail.getId());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).deptByUser(anyLong());
        doReturn(groups).when(groupRepository).findAllByUser(anyLong());
        doReturn(rights).when(rightsRepository).findByUserBox(anyLong(), anyList());
        doReturn(Optional.ofNullable(userBox)).when(userBoxRepository).findById(anyLong());
        doReturn(Optional.ofNullable(songDetail)).when(songDetailRepository).findById(anyLong());
        doReturn(userBoxSong).when(userBoxSongRepository).save(any(UserBoxSong.class));
        Long id = userBoxSongService.saveUserBoxSong(user.getId(), userBox.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(userBoxSong.getId());

        //verify
        verify(userRepository, times(1)).deptByUser(anyLong());
        verify(groupRepository, times(1)).findAllByUser(anyLong());
        verify(rightsRepository, times(1)).findByUserBox(anyLong(), anyList());
        verify(userBoxRepository, times(1)).findById(anyLong());
        verify(songDetailRepository, times(1)).findById(anyLong());
        verify(userBoxSongRepository, times(1)).save(any(UserBoxSong.class));
    }

    @DisplayName("사용자박스 곡 삭제")
    @Test
    public void 사용자박스_곡_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getNormalUser(DeptFactory.getDept(), 1L);
        List<Group> groups = GroupFactory.getGroupsWithId(user);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        UserBoxSong userBoxSong = UserBoxSongFactory.getUserBoxSong(songDetail, userBox, 1L);
        List<Rights> rights = new ArrayList<>();
        String rightType = "read,write";
        rights.add(RightsFactory.getUserBoxRightsWithUser(userBox.getId(), user.getId(), rightType, 1L));

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).deptByUser(anyLong());
        doReturn(groups).when(groupRepository).findAllByUser(anyLong());
        doReturn(rights).when(rightsRepository).findByUserBox(anyLong(), anyList());
        doReturn(Optional.ofNullable(userBoxSong)).when(userBoxSongRepository).findById(anyLong());
        doReturn(1L).when(userBoxSongRepository).delete(any(UserBoxSong.class));
        Long resValue = userBoxSongService.deleteUserBoxSong(user.getId(), userBox.getId(), userBoxSong.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).deptByUser(anyLong());
        verify(groupRepository, times(1)).findAllByUser(anyLong());
        verify(rightsRepository, times(1)).findByUserBox(anyLong(), anyList());
        verify(userBoxSongRepository, times(1)).findById(anyLong());
        verify(userBoxSongRepository, times(1)).delete(any(UserBoxSong.class));

    }

    @DisplayName("사용자박스 곡 리스트 조회")
    @Test
    public void 사용자박스_곡_리스트_조회_Service() throws Exception {

        //given
        String sort = "TITLE";
        String order = "ASC";
        int size = 10;
        int offset = 0;

        User user = UserFactory.getNormalUser(DeptFactory.getDept(), 1L);
        List<Group> groups = GroupFactory.getGroupsWithId(user);
        UserBox userBox = UserBoxFactory.getUserBox(user, 1L);
        SongMaster songMaster = SongMasterFactory.getSongMaster(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        SongDetail songDetail = SongDetailFactory.getSongDetail(songMaster, guitarCode, wordsMaster, 1L);

        UserBoxSong userBoxSong = UserBoxSongFactory.getUserBoxSong(songDetail, userBox, 1L);
        List<Rights> rights = new ArrayList<>();
        String rightType = "read";
        rights.add(RightsFactory.getUserBoxRightsWithUser(userBox.getId(), user.getId(), rightType, 1L));

        List<UserBoxSong> userBoxSongs = new ArrayList<>();
        userBoxSongs.add(userBoxSong);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).deptByUser(anyLong());
        doReturn(groups).when(groupRepository).findAllByUser(anyLong());
        doReturn(rights).when(rightsRepository).findByUserBox(anyLong(), anyList());
        doReturn(userBoxSongs.stream().map(song -> new UserBoxSongDto.UserBoxSongResponse(song.getId(), song.getSongDetail().getId(),
                song.getSongDetail().getTitle(), song.getCreateDate())).collect(Collectors.toList()))
                .when(userBoxSongRepository).findAll(anyLong(), any(SortType.class), any(OrderType.class), anyInt(), anyInt());

        List<UserBoxSongDto.UserBoxSongResponse> values = userBoxSongService.findAllUserBoxSong(user.getId(), userBox.getId(), sort, order, size, offset);

        //then
        assertThat(values.size()).isEqualTo(userBoxSongs.size());
        assertThat(values).extracting(UserBoxSongDto.UserBoxSongResponse::getSongDetailId).contains(songDetail.getId());
        assertThat(values).extracting(UserBoxSongDto.UserBoxSongResponse::getTitle).contains(songDetail.getTitle());

        //verify
        verify(userRepository, times(1)).deptByUser(anyLong());
        verify(groupRepository, times(1)).findAllByUser(anyLong());
        verify(rightsRepository, times(1)).findByUserBox(anyLong(), anyList());
        verify(userBoxSongRepository, times(1)).findAll(anyLong(), any(SortType.class), any(OrderType.class), anyInt(), anyInt());

    }

}
