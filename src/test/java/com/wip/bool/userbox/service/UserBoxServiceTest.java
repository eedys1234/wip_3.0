package com.wip.bool.userbox.service;

import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userbox.UserBoxFactory;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.dto.UserBoxDto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserBoxServiceTest {

    @InjectMocks
    private UserBoxService userBoxService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBoxRepository userBoxRepository;

    private User getNormalUser() {

        User user = UserFactory.getNormalUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private User getAdminUser() {
        User user = UserFactory.getAdminUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private UserBox getUserBox(User user) {

        UserBox userBox = UserBoxFactory.getUserBox(user);
        ReflectionTestUtils.setField(userBox, "id", 1L);
        return userBox;
    }

    private UserBox getUserBox(User user, Long id) {

        UserBox userBox = UserBoxFactory.getUserBox(user);
        ReflectionTestUtils.setField(userBox, "id", 1L);
        return userBox;
    }

    @DisplayName("사용자박스 추가")
    @Test
    public void 사용자박스_추가_Service() throws Exception {

        //given
        User user = getNormalUser();
        UserBox userBox = getUserBox(user);

        UserBoxDto.UserBoxSaveRequest requestDto = new UserBoxDto.UserBoxSaveRequest();
        ReflectionTestUtils.setField(requestDto, "userBoxName", "사용자박스_1");
        ReflectionTestUtils.setField(requestDto, "shareType", "PRIVATE");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(userBox).when(userBoxRepository).save(any(UserBox.class));
        Long id = userBoxService.addUserBox(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userBoxRepository, times(1)).save(any(UserBox.class));
    }

    @DisplayName("사용자박스 수정 일반사용자")
    @Test
    public void 사용자박스_수정_일반사용자_Service() throws Exception {

        //given
        User user = getNormalUser();
        UserBox userBox = getUserBox(user);
        UserBox update_userBox = getUserBox(user);
        update_userBox.updateUserBoxName("사용자박스_2");

        UserBoxDto.UserBoxUpdateRequest requestDto = new UserBoxDto.UserBoxUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "userBoxName", "사용자박스_2");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(userBox)).when(userBoxRepository).findById(any(Long.class), any(Long.class));
        doReturn(update_userBox).when(userBoxRepository).save(any(UserBox.class));
        Long id = userBoxService.updateUserBox(user.getId(), userBox.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(userBox.getId());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userBoxRepository, times(1)).findById(any(Long.class), any(Long.class));
        verify(userBoxRepository, times(1)).save(any(UserBox.class));
    }

    @DisplayName("사용자박스 수정 관리자")
    @Test
    public void 사용자박스_수정_관리자_Service() throws Exception {

        //given
        User user = getAdminUser();
        UserBox userBox = getUserBox(user);
        UserBox update_userBox = getUserBox(user);
        update_userBox.updateUserBoxName("사용자박스_2");

        UserBoxDto.UserBoxUpdateRequest requestDto = new UserBoxDto.UserBoxUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "userBoxName", "사용자박스_2");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(any(Long.class));
        doReturn(Optional.ofNullable(userBox)).when(userBoxRepository).findById(any(Long.class));
        doReturn(update_userBox).when(userBoxRepository).save(any(UserBox.class));
        Long id = userBoxService.updateUserBox(user.getId(), userBox.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(userBox.getId());

        //verify
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userBoxRepository, times(1)).findById(any(Long.class));
        verify(userBoxRepository, times(1)).save(any(UserBox.class));
    }

    @DisplayName("사용자박스 삭제")
    @Test
    public void 사용자박스_삭제_Service() throws Exception {

        //given
        User user = getNormalUser();
        UserBox userBox = getUserBox(user);

        //when
        doReturn(Optional.ofNullable(userBox)).when(userBoxRepository).findById(any(Long.class));
        doReturn(1L).when(userBoxRepository).delete(any(UserBox.class));
        Long resValue = userBoxService.deleteUserBox(user.getId(), userBox.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userBoxRepository, times(1)).findById(any(Long.class));
        verify(userBoxRepository, times(1)).delete(any(UserBox.class));
    }

    @DisplayName("사용자박스 리스트 조회")
    @Test
    public void 사용자박스_리스트_조회_Service() throws Exception {

        //given
        User user = getNormalUser();
        List<UserBox> userBoxes = new ArrayList<>();

        for(int i=1;i<=10;i++)
        {
            userBoxes.add(getUserBox(user, Long.valueOf(String.valueOf(i))));
        }

        List<UserBoxDto.UserBoxResponse> userBoxResponses = userBoxes.stream()
                                                                    .map(UserBoxDto.UserBoxResponse::new)
                                                                    .collect(Collectors.toList());

        String share = "PRIVATE";
        String order = "ASC";
        int size = 10;
        int offset = 0;

        //when
        doReturn(userBoxResponses).when(userBoxRepository).findAll(any(Long.class), any(OrderType.class), any(List.class), any(Integer.class), any(Integer.class));
//        List<UserBoxDto.UserBoxResponse> values = userBoxService.findAll(user.getId(), order, share, size, offset);

        //then

        //private,
    }


}
