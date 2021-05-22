package com.wip.bool.userbox.repository;

import com.wip.bool.cmmn.rights.RightsFactory;
import com.wip.bool.cmmn.type.OrderType;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.cmmn.userbox.UserBoxFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepository;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.userbox.domain.UserBox;
import com.wip.bool.userbox.domain.UserBoxRepository;
import com.wip.bool.userbox.dto.UserBoxDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RightsRepository rightsRepository;

    @DisplayName("사용자박스 추가")
    @Test
    public void 사용자박스_추가_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        UserBox userBox = UserBoxFactory.getUserBox(user);

        //when
        UserBox addUserBox = userBoxRepository.save(userBox);

        //then
        assertThat(addUserBox.getUserBoxName()).isEqualTo(userBox.getUserBoxName());
    }

    @DisplayName("사용자박스 수정")
    @Test
    public void 사용자박스_수정_Repository() throws Exception {

        //given
        User user = UserFactory.getNormalUser();
        UserBox userBox = UserBoxFactory.getUserBox(user);
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
        User user = UserFactory.getNormalUser();
        UserBox userBox = UserBoxFactory.getUserBox(user);
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
        User user = UserFactory.getNormalUser();
        User addUser = userRepository.save(user);
        List<UserBox> userBoxes = UserBoxFactory.getUserBoxes(user);
        List<String> userBoxNames = UserBoxFactory.getUserBoxNames();

        int size = 10;
        int offset = 0;

        for(UserBox userBox : userBoxes)
        {
            userBoxRepository.save(userBox);
        }

        Rights rights = RightsFactory.getUserBoxRightsWithUser(userBoxes.get(0).getId(), user.getId());
        rightsRepository.save(rights);

        //when
        List<UserBoxDto.UserBoxResponse> values = userBoxRepository.findAll(OrderType.ASC, size, offset, addUser.getId());

        //then
        assertThat(values.size()).isEqualTo(1);
        assertThat(values).extracting(UserBoxDto.UserBoxResponse::getUserBoxName).contains(userBoxNames.get(0));
    }


}
