package com.wip.bool.rights.service;

import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepository;
import com.wip.bool.rights.dto.RightDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RightService {

    private final RightsRepository rightRepository;

    @Transactional
    public String saveRight(RightDto.RightSaveRequest requestDto) {

        List<String> rightTypes = Arrays.stream(requestDto.getRightType().split(","))
                                                                        .collect(Collectors.toList());


        List<Long> ids = new ArrayList<>();
        for(String rightType : rightTypes)
        {
            Rights rights = requestDto.toEntity();
            rights.updateRightType(Rights.RightType.valueOf(rightType.toUpperCase()));
            ids.add(rightRepository.save(rights).getId());
        }

        return ids.stream()
                .map(id -> String.valueOf(id))
                .collect(Collectors.joining(","));
    }

    @Transactional
    public Long deleteRight(Long rightId) {

        Rights right = rightRepository.findById(rightId).
            orElseThrow(() -> new EntityNotFoundException(rightId, ErrorCode.NOT_FOUND_RIGHT));

        rightRepository.delete(right);
        return 1L;
    }
}
