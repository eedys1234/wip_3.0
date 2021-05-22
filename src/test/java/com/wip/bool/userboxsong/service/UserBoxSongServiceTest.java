package com.wip.bool.userboxsong.service;

import com.wip.bool.group.domain.GroupRepository;
import com.wip.bool.music.song.domain.SongDetailRepository;
import com.wip.bool.rights.domain.RightsRepositoryImpl;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.domain.UserBoxSongRepository;
import com.wip.bool.userbox.service.UserBoxSongService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

//        //given
//        User user = UserFactory.getNormalUser(1L);
//        List<Group> groups = GroupFactory.getGroups();
//
//        //when
//        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
//        doReturn(groups).when(groupRepository).findAllByUser(anyLong());
//        doReturn(rights).when(rightsRepository).findByUserBox(anyLong(), anyList());
//        doReturn(Optional.ofNullable(userBox)).when(userBoxRepository).findById(anyLong());
//        doReturn(Optional.ofNullable(songDetail)).when(songDetailRepository).findById(anyLong());
//        doReturn(userBoxSong.getId()).when(userBoxSongRepository).save(any(UserBoxSong.class));
//
//        //then
//
//        //verify
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(groupRepository, times(1)).findAllByUser(anyLong());
//        verify(rightsRepository, times(1)).findByUserBox(anyLong(), anyList());
//        verify(userBoxRepository, times(1)).findById(anyLong());
//        verify(songDetailRepository, times(1)).findById(anyLong());
//        verify(userBoxSongRepository, times(1)).save(any(UserBoxSong.class));
    }

    @DisplayName("사용자박스 곡 삭제")
    @Test
    public void 사용자박스_곡_삭제_Service() throws Exception {

        //given
        //when
        //then
        //verify
    }

    @DisplayName("사용자박스 곡 리스트 조회")
    @Test
    public void 사용자박스_곡_리스트_조회_Service() throws Exception {

        //given
        //when
        //then
        //verify
    }

}
