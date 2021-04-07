package com.wip.bool.service.position;

import com.wip.bool.domain.cmmn.CodeMapper;
import com.wip.bool.domain.position.Position;
import com.wip.bool.domain.position.PositionRepository;
import com.wip.bool.web.dto.position.PositionDto;
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

        return position.getId();
    }

    @Transactional(readOnly = true)
    public List<PositionDto.PositionResponse> findAll() {

        List<PositionDto.PositionResponse> list = null;

        if(Objects.isNull(codeMapper.get("position"))) {

            list = positionRepository.findAll().stream()
                    .map(PositionDto.PositionResponse::new)
                    .collect(Collectors.toList());

            codeMapper.put("position", list);

        }
        else {
            list = (List<PositionDto.PositionResponse>) codeMapper.get("position").get("position");
        }

        return list;
    }

    public PositionDto.PositionResponse findOne(Long positionId) {

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("직위가 존재하지 않습니다. id = " + positionId));
        return new PositionDto.PositionResponse(position);
    }
}
