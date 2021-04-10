package com.wip.bool.service.detp;

import com.wip.bool.domain.cmmn.CodeMapper;
import com.wip.bool.domain.dept.Dept;
import com.wip.bool.domain.dept.DeptRepository;
import com.wip.bool.web.dto.dept.DeptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;

    private final CodeMapper codeMapper;

    public Long add(DeptDto.DeptSaveRequest requestDto) {
        return deptRepository.save(requestDto.toEntity()).getId();
    }

    public Long update(Long deptId, DeptDto.DeptUpdateRequest requestDto) {

        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + deptId));

        dept.update(requestDto.getDeptName());
        return deptRepository.save(dept).getId();
    }

    @Transactional(readOnly = true)
    public List<DeptDto.DeptResponse> findAll() {

        final String DEPT_KEY = "dept";
        List<DeptDto.DeptResponse> list = null;

        if(Objects.isNull(codeMapper.get(DEPT_KEY))) {
            list =  deptRepository.findAll().stream()
                                            .map(DeptDto.DeptResponse::new)
                                            .collect(Collectors.toList());

            codeMapper.put(DEPT_KEY, list);
        }
        else {
            list = (List<DeptDto.DeptResponse>) codeMapper.get(DEPT_KEY).get(DEPT_KEY);
        }

        return list;
    }

    public DeptDto.DeptResponse findOne(Long deptId) {

        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(()-> new IllegalArgumentException("부서가 존재하지 않습니다. id = " + deptId));

        return new DeptDto.DeptResponse(dept);
    }
}
