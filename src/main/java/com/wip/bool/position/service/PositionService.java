package com.wip.bool.position.service;

import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.position.domain.Position;
import com.wip.bool.position.domain.PositionRepository;
import com.wip.bool.position.dto.PositionDto;
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
public class PositionService {

    private final PositionRepository positionRepository;

    private final UserRepository userRepository;

    @Transactional
    public Long savePosition(Long userId, PositionDto.PositionSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            return positionRepository.save(requestDto.toEntity()).getId();
        }
        else {
            throw new AuthorizationException();
        }

    }

    @Transactional
    public Long updatePosition(Long userId, Long positionId, PositionDto.PositionUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            Position position = positionRepository.findById(positionId)
                    .orElseThrow(() -> new IllegalArgumentException("직위가 존재하지 않습니다. id = " + positionId));

            position.updatePositionName(requestDto.getPositionName());
            return position.getId();
        }
        else {
            throw new AuthorizationException();
        }

    }

    @Transactional(readOnly = true)
    public List<PositionDto.PositionResponse> findAll() {
       return positionRepository.findAll().stream()
               .map(PositionDto.PositionResponse::new)
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PositionDto.PositionResponse findOne(Long positionId) {

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("직위가 존재하지 않습니다. id = " + positionId));
        return new PositionDto.PositionResponse(position);
    }

    public Long deletePosition(Long userId, Long positionId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException());

        positionRepository.delete(position);
        return 1L;
    }
}
