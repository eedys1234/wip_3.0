package com.wip.bool.bible.repository;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.configure.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class WordsMaterRepositoryTest {

    @Autowired
    private WordsMasterRepository wordsMasterRepository;

    @DisplayName("단원 추가")
    @Test
    public void 단원_추가_Repository() throws Exception {

        //given
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster();

        //when
        WordsMaster addWordsMaster = wordsMasterRepository.save(wordsMaster);

        //then
        List<WordsMaster> wordsMasters = wordsMasterRepository.findAll();
        assertThat(addWordsMaster.getId()).isGreaterThan(0L);
        assertThat(addWordsMaster.getId()).isEqualTo(wordsMasters.get(0).getId());
        assertThat(addWordsMaster.getWordsName()).isEqualTo(wordsMasters.get(0).getWordsName());
        assertThat(addWordsMaster.getWordsOrder()).isEqualTo(wordsMasters.get(0).getWordsOrder());
    }

    @DisplayName("단원 삭제")
    @Test
    public void 단원_삭제_Repository() throws Exception {

        //given
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster();
        WordsMaster addWordsMaster = wordsMasterRepository.save(wordsMaster);

        //when
        Long resValue = wordsMasterRepository.delete(addWordsMaster);

        //then
        assertThat(resValue).isEqualTo(1L);
    }

    @DisplayName("단원 리스트 조회")
    @Test
    public void 단원_리스트_조회_Repository() throws Exception {

        //given
        List<WordsMaster> wordsMasters = WordsMasterFactory.getWordsMasters();

        for(WordsMaster wordsMaster : wordsMasters)
        {
            wordsMasterRepository.save(wordsMaster);
        }

        //when
        List<WordsMaster> values = wordsMasterRepository.findAll();

        //then
        assertThat(values.size()).isEqualTo(wordsMasters.size());
        assertThat(values).extracting(WordsMaster::getWordsName).containsAll(WordsMasterFactory.getWordsNames());
    }
}
