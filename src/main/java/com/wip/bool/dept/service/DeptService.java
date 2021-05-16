package com.wip.bool.dept.service;

import com.wip.bool.cmmn.CodeMapper;
import com.wip.bool.dept.domain.Dept;
import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.dept.dto.DeptDto;
import com.wip.bool.exception.excp.AuthorizationException;
import com.wip.bool.user.domain.Role;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;

    private final UserRepository userRepository;

    private final CodeMapper codeMapper;

    public Long saveDept(Long userId, DeptDto.DeptSaveRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());

        Role role = user.getRole();
        if(role == Role.ROLE_ADMIN) {
            return deptRepository.save(requestDto.toEntity()).getId();
        }
        else {
            throw new AuthorizationException();
        }

    }

    public Long updateDept(Long userId, Long deptId, DeptDto.DeptUpdateRequest requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {

            Dept dept = deptRepository.findById(deptId)
                    .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + deptId));

            dept.update(requestDto.getDeptName());
            return deptRepository.save(dept).getId();
        }
        else {
            throw new AuthorizationException();
        }
    }

    @Transactional(readOnly = true)
    public List<DeptDto.DeptResponse> findAll() {

//        final String DEPT_KEY = "dept";
//        List<DeptDto.DeptResponse> list = null;
//
//        if(Objects.isNull(codeMapper.get(DEPT_KEY))) {
//            list =  deptRepository.findAll().stream()
//                                            .map(DeptDto.DeptResponse::new)
//                                            .collect(Collectors.toList());
//
//            codeMapper.put(DEPT_KEY, list);
//        }
//        else {
//            list = (List<DeptDto.DeptResponse>) codeMapper.get(DEPT_KEY).get(DEPT_KEY);
//        }

        return deptRepository.findAll().stream()
                                        .map(DeptDto.DeptResponse::new)
                                        .collect(Collectors.toList());
    }

    public DeptDto.DeptResponse findOne(Long deptId) {

        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + deptId));

        return new DeptDto.DeptResponse(dept);
    }

    public Long deleteDept(Long userId, Long deptId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());

        Role role = user.getRole();

        if(role == Role.ROLE_ADMIN) {
            Dept dept = deptRepository.findById(deptId)
                    .orElseThrow(() -> new IllegalArgumentException());

            deptRepository.delete(dept);
            return 1L;
        }
        else {
            throw new AuthorizationException();
        }
    }
}
