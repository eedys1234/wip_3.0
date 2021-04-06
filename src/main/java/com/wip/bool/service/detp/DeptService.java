package com.wip.bool.service.detp;

import com.wip.bool.domain.dept.Dept;
import com.wip.bool.domain.dept.DeptRepository;
import com.wip.bool.web.dto.dept.DeptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;

    public Long add(DeptDto.DeptSaveRequest requestDto) {
        return deptRepository.save(requestDto.toEntity()).getId();
    }

    public Long update(Long deptId, DeptDto.DeptUpdateRequest requestDto) {

        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + deptId));

        return deptRepository.save(dept).getId();
    }

    public List<DeptDto.DeptResponse> findAll() {
        return deptRepository.findAll().stream()
                .map(DeptDto.DeptResponse::new)
                .collect(Collectors.toList());
    }

    public DeptDto.DeptResponse findOne(Long deptId) {

        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + deptId));

        return new DeptDto.DeptResponse(dept);
    }
}
