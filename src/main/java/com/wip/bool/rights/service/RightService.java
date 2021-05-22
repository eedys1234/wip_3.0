package com.wip.bool.rights.service;

import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepository;
import com.wip.bool.rights.dto.RightDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RightService {

    private final RightsRepository rightRepository;

    @Transactional
    public Long saveRight(RightDto.RightSaveRequest requestDto) {

        return rightRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long deleteRight(Long rightId, String rightType) {

        Rights rights = rightRepository.findById(rightId)
            .orElseThrow(() -> new EntityNotFoundException(rightId, ErrorCode.NOT_FOUND_RIGHT));

        Long rightValues = Arrays.stream(rightType.split(","))
                .mapToLong(right -> Rights.RightType.valueOf(right.toUpperCase()).getValue())
                .sum();

        Long deleteRightType = rights.getRightType() & rightValues;

        if(deleteRightType == 0) {
            throw new IllegalStateException();
        }

        //권한 레코드 삭제
        if(rights.getRightType() - deleteRightType == 0) {
            rightRepository.delete(rights);
        }
        //권한 삭제
        else {
            rights.updateRightType(rights.getRightType() - deleteRightType);
        }

        return 1L;
    }
}
