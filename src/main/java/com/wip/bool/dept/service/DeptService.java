package com.wip.bool.dept.service;

import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.dept.dto.DeptDto;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.exception.excp.not_found.NotFoundDeptException;
import com.wip.bool.exception.excp.not_found.NotFoundUserException;
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
                .orElseThrow(() -> new NotFoundUserException(userId));

        Role role = user.getRole();
        if(role == Role.ROLE_ADMIN) {
            return deptRepository.save(requestDto.toEntity()).getId();
        }
        else {
            throw new AuthorizationException();
        }

    }

    @Transactional
    public Long updateDept(Long userId, Long deptId, DeptDto.DeptUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {

            Dept dept = deptRepository.findById(deptId)
                    .orElseThrow(()-> new NotFoundDeptException(deptId));

            dept.update(requestDto.getDeptName());
            return deptRepository.save(dept).getId();
        }
        else {
            throw new AuthorizationException();
        }
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
                .orElseThrow(()-> new NotFoundDeptException(deptId));

        return new DeptDto.DeptResponse(dept);
    }

    @Transactional
    public Long deleteDept(Long userId, Long deptId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            Dept dept = deptRepository.findById(deptId)
                    .orElseThrow(() -> new NotFoundDeptException(deptId));

            deptRepository.delete(dept);
            return 1L;
        }
        else {
            throw new AuthorizationException();
        }
    }
}
