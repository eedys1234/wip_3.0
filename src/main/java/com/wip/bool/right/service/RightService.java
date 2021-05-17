package com.wip.bool.right.service;

import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.right.domain.Right;
import com.wip.bool.right.domain.RightRepository;
import com.wip.bool.right.dto.RightDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RightService {

    private final RightRepository rightRepository;

    @Transactional
    public Long saveRight(RightDto.RightSaveRequest requestDto) {
        return rightRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long deleteRight(Long rightId) {

        Right right = rightRepository.findById(rightId).
            orElseThrow(() -> new EntityNotFoundException(rightId, ErrorCode.NOT_FOUND_RIGHT));

        rightRepository.delete(right);
        return 1L;
    }
}
