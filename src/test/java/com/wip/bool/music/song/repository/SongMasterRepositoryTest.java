package com.wip.bool.music.song.repository;

import com.wip.bool.cmmn.music.song.SongMasterFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.music.song.domain.SongMaster;
import com.wip.bool.music.song.domain.SongMasterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class SongMasterRepositoryTest {

    @Autowired
    private SongMasterRepository songMasterRepository;

    @DisplayName("Song Master 추가")
    @Test
    public void SongMaster_추가_Repository() throws Exception {

        //given
        SongMaster songMaster = SongMasterFactory.getSongMaster();

        //when
        SongMaster addSongMaster = songMasterRepository.save(songMaster);

        //then
        List<SongMaster> songMasters = songMasterRepository.findAll();
        assertThat(addSongMaster.getId()).isGreaterThan(0L);
        assertThat(addSongMaster.getId()).isEqualTo(songMasters.get(0).getId());
        assertThat(addSongMaster.getCodeKey()).isEqualTo(songMasters.get(0).getCodeKey());
        assertThat(addSongMaster.getCodeName()).isEqualTo(songMasters.get(0).getCodeName());
    }

    @DisplayName("Song Master 삭제")
    @Test
    public void SongMaster_삭제_Repository() throws Exception {

        //given
        SongMaster songMaster = SongMasterFactory.getSongMaster();
        SongMaster addSongMaster = songMasterRepository.save(songMaster);

        //when
        Long resValue = songMasterRepository.delete(addSongMaster);

        //then
        List<SongMaster> songMasters = songMasterRepository.findAll();
        assertThat(resValue).isEqualTo(1L);
        assertThat(songMasters.size()).isEqualTo(0);
    }

    @DisplayName("Song Master 리스트 조회")
    @Test
    public void SongMaster_리스트_조회_Repository() throws Exception {

        //given
        List<SongMaster> songMasters = SongMasterFactory.getSongMasters();

        for(SongMaster songMaster : songMasters)
        {
            songMasterRepository.save(songMaster);
        }

        //when
        List<SongMaster> values = songMasterRepository.findAll();

        //then
        assertThat(values.size()).isEqualTo(songMasters.size());
        assertThat(values).extracting(SongMaster::getCodeName).containsAll(songMasters
                .stream()
                .map(SongMaster::getCodeName)
                .collect(Collectors.toList()));

        assertThat(values).extracting(SongMaster::getId).containsAll(songMasters
                .stream()
                .map(SongMaster::getId)
                .collect(Collectors.toList()));

        assertThat(values).extracting(SongMaster::getCodeKey).containsAll(songMasters
                .stream()
                .map(SongMaster::getCodeKey)
                .collect(Collectors.toList()));

    }
}
