package com.wip.bool.bible.service;

import com.wip.bool.bible.domain.WordsMaster;
import com.wip.bool.bible.domain.WordsMasterRepository;
import com.wip.bool.bible.dto.WordsMasterDto;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordsMasterService {

    private final UserRepository userRepository;
    private final WordsMasterRepository wordsMasterRepository;

    @Transactional
    public Long saveWordsMaster(Long userId, WordsMasterDto.WordsMasterSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        Integer order = wordsMasterRepository.findCount();
        WordsMaster wordsMaster = WordsMaster.createWordsMaster(requestDto.getWordsName(), order);

        return wordsMasterRepository.save(wordsMaster).getId();
    }

    @Transactional
    public Long deleteWordsMaster(Long userId, Long wordsMasterId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        WordsMaster wordsMaster = wordsMasterRepository.findById(wordsMasterId)
                .orElseThrow(() -> new EntityNotFoundException(wordsMasterId, ErrorCode.NOT_FOUND_WORDS_MASTER));

        return wordsMasterRepository.delete(wordsMaster);
    }

    @Transactional(readOnly = true)
    public List<WordsMasterDto.WordsMasterResponse> findAll() {
        return wordsMasterRepository.findAll()
                .stream()
                .map(WordsMasterDto.WordsMasterResponse::new)
                .collect(Collectors.toList());
    }
}
