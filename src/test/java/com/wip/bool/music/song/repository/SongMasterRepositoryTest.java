package com.wip.bool.music.song.repository;

import com.wip.bool.configure.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;


@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class SongMasterRepositoryTest {
}
