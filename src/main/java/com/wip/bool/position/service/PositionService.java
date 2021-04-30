package com.wip.bool.position.service;

import com.wip.bool.cmmn.CodeMapper;
import com.wip.bool.position.domain.Position;
import com.wip.bool.position.domain.PositionRepository;
import com.wip.bool.position.dto.PositionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    private final CodeMapper codeMapper;

    public Long add(PositionDto.PositionSaveRequest requestDto) {

        return positionRepository.save(requestDto.toEntity()).getId();
    }

    public Long update(Long positionId, PositionDto.PositionUpdateRequest requestDto) {

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("직위가 존재하지 않습니다. id = " + positionId));

        position.update(requestDto.getPositionName());
        return position.getId();
    }

    @Transactional(readOnly = true)
    public List<PositionDto.PositionResponse> findAll() {

        final String POSITION_KEY = "position";
        List<PositionDto.PositionResponse> list = null;

        if(Objects.isNull(codeMapper.get(POSITION_KEY))) {

            list = positionRepository.findAll().stream()
                    .map(PositionDto.PositionResponse::new)
                    .collect(Collectors.toList());

            codeMapper.put(POSITION_KEY, list);

        }
        else {
            list = (List<PositionDto.PositionResponse>) codeMapper.get(POSITION_KEY).get(POSITION_KEY);
        }

        return list;
    }

    public PositionDto.PositionResponse findOne(Long positionId) {

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("직위가 존재하지 않습니다. id = " + positionId));
        return new PositionDto.PositionResponse(position);
    }
}
