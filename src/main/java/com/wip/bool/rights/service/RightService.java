package com.wip.bool.rights.service;

import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepository;
import com.wip.bool.rights.dto.RightDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RightService {

    private final RightsRepository rightRepository;

    @Transactional
    public Long saveRight(RightDto.RightSaveRequest requestDto) {
        return rightRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long deleteRight(Long rightId) {

        Rights right = rightRepository.findById(rightId).
            orElseThrow(() -> new EntityNotFoundException(rightId, ErrorCode.NOT_FOUND_RIGHT));

        rightRepository.delete(right);
        return 1L;
    }
}
