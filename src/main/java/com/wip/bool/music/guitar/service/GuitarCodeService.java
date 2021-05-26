package com.wip.bool.music.guitar.service;

import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.guitar.dto.GuitarCodeDto;
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
public class GuitarCodeService {

    private final GuitarCodeRepository guitarCodeRepository;

    private final UserRepository userRepository;

    @Transactional
    public Long saveGuitarCode(Long userId, GuitarCodeDto.GuitarCodeSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        int order = guitarCodeRepository.maxOrder();
        GuitarCode guitarCode = GuitarCode.createGuitarCode(requestDto.getCode(), order + 1);
        return guitarCodeRepository.save(guitarCode).getId();
    }

    @Transactional(readOnly = true)
    public List<GuitarCodeDto.GuitarCodeResponse> getGuitarCodes() {
        return guitarCodeRepository.findAll()
                .stream()
                .map(GuitarCodeDto.GuitarCodeResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long deleteGuitarCode(Long userId, Long guitarCodeId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();
        if(role != Role.ROLE_ADMIN) {
            throw new AuthorizationException();
        }

        GuitarCode guitarCode = guitarCodeRepository.findById(guitarCodeId)
                    .orElseThrow(() -> new EntityNotFoundException(guitarCodeId, ErrorCode.NOT_FOUND_GUITAR_CODE));
        return guitarCodeRepository.delete(guitarCode);
    }

}
