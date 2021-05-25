package com.wip.bool.user.service;

import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.position.domain.Position;
import com.wip.bool.position.domain.PositionRepository;
import com.wip.bool.user.domain.*;
import com.wip.bool.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final DeptRepository deptRepository;

    private final PositionRepository positionRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long join(UserDto.UserSaveRequest requestDto) {

        duplicationUser(requestDto.getEmail());

        //password hashing
        //db함수를 이용할 경우 dbms에 종속적이기 때문에 application에서 hashing 함수 적용
        User user = User.createUser(requestDto.getEmail(), requestDto.getName(), passwordEncoder.encode(requestDto.getUserPassword()),
                "", UserType.WIP, Role.ROLE_REQUEST);

        return userRepository.save(user).getId();
    }

    @Transactional
    public Long update(Long userId, UserDto.UserUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        if(!Objects.isNull(requestDto.getDeptId())) {
            Dept dept = deptRepository.findById(requestDto.getDeptId())
                    .orElseThrow(()-> new EntityNotFoundException(requestDto.getDeptId(), ErrorCode.NOT_FOUND_DEPT));

            user.updateDept(dept);
        }

        if(!Objects.isNull(requestDto.getPositionId())) {
            Position position = positionRepository.findById(requestDto.getPositionId())
                    .orElseThrow(()-> new EntityNotFoundException(requestDto.getPositionId(), ErrorCode.NOT_FOUND_POSITION));

            user.updatePosition(position);
        }

        return user.getId();
    }

    @Transactional
    public Long approve(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        user.approve();
        return user.getId();
    }

    @Transactional
    public Long delete(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        return userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto.UserResponse> findAllByRole(Role role) {
        return userRepository.findAllByRole(role).stream()
                .map(UserDto.UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto.UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto.UserResponse::new)
                .collect(Collectors.toList());
    }

    private void duplicationUser(String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        if(!Objects.isNull(user)) {
            throw new IllegalStateException("");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .map(u -> new CustomUser(u, Collections.singleton(new SimpleGrantedAuthority(u.getRole().getKey()))))
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_USER));
    }
}

