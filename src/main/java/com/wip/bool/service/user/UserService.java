package com.wip.bool.service.user;

import com.wip.bool.domain.dept.Dept;
import com.wip.bool.domain.dept.DeptRepository;
import com.wip.bool.domain.position.Position;
import com.wip.bool.domain.position.PositionRepository;
import com.wip.bool.domain.user.Role;
import com.wip.bool.domain.user.User;
import com.wip.bool.domain.user.UserConfig;
import com.wip.bool.domain.user.UserRepository;
import com.wip.bool.web.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final DeptRepository deptRepository;

    private final PositionRepository positionRepository;

    public Long register(UserDto.UserRegisterRequest requestDto) {

        duplicationUser(requestDto.getUserId());

        //부서 정보
        Dept dept = deptRepository.findById(requestDto.getDeptId())
                .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + requestDto.getDeptId()));

        //직위 정보
        Position position = positionRepository.findById(requestDto.getPositionId())
                .orElseThrow(()-> new IllegalArgumentException("직위가 존재하지 않습니다. id = " + requestDto.getPositionId()));

        User user = User.createUser(dept, position, Role.REQUEST, UserConfig.createUserConfig());
        return userRepository.save(user).getId();
    }

    public Long update(Long userId, UserDto.UserUpdateRequest requestDto) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));

        if(!Objects.isNull(requestDto.getDeptId())) {
            Dept dept = deptRepository.findById(requestDto.getDeptId())
                    .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + requestDto.getDeptId()));

            user.updateDept(dept);
        }

        if(!Objects.isNull(requestDto.getPositionId())) {
            Position position = positionRepository.findById(requestDto.getPositionId())
                    .orElseThrow(()-> new IllegalArgumentException("직위가 존재하지 않습니다. id = " + requestDto.getPositionId()));

            user.updatePosition(position);
        }

        user.updatePassword(requestDto.getUserPassword());

        return userRepository.save(user).getId();
    }

    public Long approve(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));

        user.approve();

        return userRepository.save(user).getId();
    }

    public int delete(Long userId) {
        return userRepository.delete(userId);
    }

    @Transactional(readOnly = true)
    public List<UserDto.UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserDto.UserResponse::new).collect(Collectors.toList());
    }

    private void duplicationUser(String userId) {

        User user = userRepository.findByUserId(userId);

        if(!Objects.isNull(user)) {
            throw new IllegalStateException("");
        }
    }
}
