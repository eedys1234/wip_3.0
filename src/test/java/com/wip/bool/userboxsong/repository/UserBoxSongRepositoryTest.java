package com.wip.bool.userboxsong.repository;

import com.wip.bool.configure.TestConfig;
import com.wip.bool.userbox.domain.UserBoxSongRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class UserBoxSongRepositoryTest {

    @Autowired
    private UserBoxSongRepository userBoxSongRepository;


    @DisplayName("사용자박스 곡 추가")
    @Test
    public void 사용자박스_곡_추가_Repository() throws Exception {

        //given
        //when
        //then

    }
}
