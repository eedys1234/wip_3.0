package com.wip.bool.userbox.repository;

import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userbox.UserBoxFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@Transactional
public class UserBoxRepositoryTest {

    @Autowired
    private UserBoxRepository userBoxRepository;

    @Autowired
    private UserRepository userRepository;

    private User getUser() {
        return UserFactory.getNormalUser();
    }

    private UserBox getUserBox(User user) {
        UserBox userBox = UserBoxFactory.getUserBox(user);
        return userBox;
    }

    private UserBox getUserBox(User user, String userBoxName) {
        UserBox userBox = UserBoxFactory.getUserBox(user, userBoxName);
        return userBox;
    }
    
    @DisplayName("사용자박스 추가")
    @Test
    public void 사용자박스_추가_Repository() throws Exception {

        //given
        User user = getUser();
        UserBox userBox = getUserBox(user);

        //when
        UserBox addUserBox = userBoxRepository.save(userBox);

        //then
        assertThat(addUserBox.getUserBoxName()).isEqualTo(userBox.getUserBoxName());
    }

    @DisplayName("사용자박스 수정")
    @Test
    public void 사용자박스_수정_Repository() throws Exception {

        //given
        User user = getUser();
        UserBox userBox = getUserBox(user);
        UserBox addUserBox = userBoxRepository.save(userBox);
        String updateUserBoxName = "사용자박스_2";

        //when
        addUserBox.updateUserBoxName(updateUserBoxName);
        addUserBox = userBoxRepository.save(addUserBox);

        //then
        assertThat(addUserBox.getUserBoxName()).isEqualTo(updateUserBoxName);
    }

    @DisplayName("사용자박스 삭제")
    @Test
    public void 사용자박스_삭제_Repository() throws Exception {

        //given
        User user = getUser();
        UserBox userBox = getUserBox(user);
        UserBox addUserBox = userBoxRepository.save(userBox);

        //when
        Long resValue = userBoxRepository.delete(addUserBox);

        //then
        assertThat(resValue).isEqualTo(1L);

    }

    @DisplayName("사용자박스 리스트 조회")
    @Test
    public void 사용자박스_리스트_조회_Repository() throws Exception {

        //given
        User user = getUser();
        User addUser = userRepository.save(user);
        String userBoxName = "사용자박스_";
        List<UserBox> userBoxes = new ArrayList<>();
        List<String> userBoxNames = new ArrayList<>();
        List<ShareType> shareTypes = new ArrayList<>();
        shareTypes.add(ShareType.PRIVATE);
        int size = 10;
        int offset = 0;
        for(int i=1;i<=10;i++)
        {
            userBoxNames.add(userBoxName + i);
            UserBox userBox = getUserBox(addUser, userBoxName + i);

            userBoxes.add(userBoxRepository.save(userBox));
        }

        //when
        List<UserBox> values = userBoxRepository.findAll(addUser.getId(), OrderType.ASC, size, offset);

        //then
        assertThat(values.size()).isEqualTo(userBoxes.size());
        assertThat(values).extracting(UserBox::getUserBoxName).containsAll(userBoxNames);
    }


}
