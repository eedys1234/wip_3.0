package com.wip.bool.bible.service;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.bible.dto.WordsMasterDto;
import com.wip.bool.cmmn.bible.WordsMasterFactory;
import com.wip.bool.cmmn.user.UserFactory;
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
public class WordsMasterServiceTest {

    @InjectMocks
    private WordsMasterService wordsMasterService;

    @Mock
    private WordsMasterRepository wordsMasterRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("단원 추가")
    @Test
    public void 단원_추가_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);
        WordsMasterDto.WordsMasterSaveRequest requestDto = new WordsMasterDto.WordsMasterSaveRequest();
        ReflectionTestUtils.setField(requestDto, "wordsName", wordsMaster.getWordsName());

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(1).when(wordsMasterRepository).findCount();
        doReturn(wordsMaster).when(wordsMasterRepository).save(any(WordsMaster.class));
        Long id = wordsMasterService.saveWordsMaster(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(wordsMaster.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(wordsMasterRepository, times(1)).findCount();
        verify(wordsMasterRepository, times(1)).save(any(WordsMaster.class));
    }

    @DisplayName("단원 삭제")
    @Test
    public void 단원_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        WordsMaster wordsMaster = WordsMasterFactory.getWordsMaster(1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(wordsMaster)).when(wordsMasterRepository).findById(anyLong());
        doReturn(1L).when(wordsMasterRepository).delete(any(WordsMaster.class));
        Long resValue = wordsMasterService.deleteWordsMaster(user.getId(), wordsMaster.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(wordsMasterRepository, times(1)).findById(anyLong());
        verify(wordsMasterRepository, times(1)).delete(any(WordsMaster.class));
    }

    @DisplayName("단원 리스트 조회")
    @Test
    public void 단원_리스트_조회_Service() throws Exception {

        //given
        List<WordsMaster> wordsMasters = WordsMasterFactory.getWordsMastersWithId();

        //when
        doReturn(wordsMasters).when(wordsMasterRepository).findAll();
        List<WordsMasterDto.WordsMasterResponse> values = wordsMasterService.findAll();

        // then
        assertThat(values.size()).isEqualTo(wordsMasters.size());
        assertThat(values).extracting(WordsMasterDto.WordsMasterResponse::getWordsName)
            .containsAll(wordsMasters.stream()
                                    .map(WordsMaster::getWordsName)
                                    .collect(Collectors.toList()));

        assertThat(values).extracting(WordsMasterDto.WordsMasterResponse::getWordsMasterId)
                .containsAll(wordsMasters.stream()
                                        .map(WordsMaster::getId)
                                        .collect(Collectors.toList()));

        assertThat(values).extracting(WordsMasterDto.WordsMasterResponse::getWordsOrder)
                .containsAll(wordsMasters.stream()
                                        .map(WordsMaster::getWordsOrder)
                                        .collect(Collectors.toList()));

        // verify
        verify(wordsMasterRepository, times(1)).findAll();
    }
}
