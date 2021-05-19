package com.wip.bool.dept.service;

import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.dept.dto.DeptDto;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
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
public class DeptService {

    private final DeptRepository deptRepository;

    private final UserRepository userRepository;

    @Transactional
    public Long saveDept(Long userId, DeptDto.DeptSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();
        if(role == Role.ROLE_ADMIN) {
            return deptRepository.save(requestDto.toEntity()).getId();
        }

        throw new AuthorizationException();

    }

    @Transactional
    public Long updateDept(Long userId, Long deptId, DeptDto.DeptUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {

            Dept dept = deptRepository.findById(deptId)
                    .orElseThrow(()-> new EntityNotFoundException(deptId, ErrorCode.NOT_FOUND_DEPT));

            dept.update(requestDto.getDeptName());
            return deptRepository.save(dept).getId();
        }

        throw new AuthorizationException();
    }

    @Transactional(readOnly = true)
    public List<DeptDto.DeptResponse> findAll() {

        return deptRepository.findAll().stream()
                                        .map(DeptDto.DeptResponse::new)
                                        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeptDto.DeptResponse findOne(Long deptId) {

        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(()-> new EntityNotFoundException(deptId, ErrorCode.NOT_FOUND_DEPT));

        return new DeptDto.DeptResponse(dept);
    }

    @Transactional
    public Long deleteDept(Long userId, Long deptId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            Dept dept = deptRepository.findById(deptId)
                    .orElseThrow(()-> new EntityNotFoundException(deptId, ErrorCode.NOT_FOUND_DEPT));

            deptRepository.delete(dept);
            return 1L;
        }

        throw new AuthorizationException();
    }
}
