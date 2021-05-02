package com.wip.bool.dept;

import com.wip.bool.dept.domain.DeptRepository;
import com.wip.bool.dept.service.DeptService;
import com.wip.bool.dept.dto.DeptDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeptControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private DeptService deptService;

    @After
    public void tearDown() throws Exception {
        deptRepository.deleteAll();
    }

    @Test
    public void 부서_추가() throws Exception {

        //given
        String deptName = "밍공";

        DeptDto.DeptSaveRequest requestDto = DeptDto.DeptSaveRequest.builder()
                                                    .deptName(deptName)
                                                    .build();

        String url = "http://localhost:" + port + "/api/v1/dept";

        //when
        ResponseEntity<Long> entity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(entity.getBody()).isGreaterThan(0L);
    }

    @Test
    public void 부서_수정() throws Exception {

        //given
        String deptName = "밍공";

        DeptDto.DeptSaveRequest saveRequestDto = DeptDto.DeptSaveRequest.builder()
                .deptName(deptName)
                .build();

        Long id = deptRepository.save(saveRequestDto.toEntity()).getId();

        String url = "http://localhost:" + port + "api/v1/dept/" + id;
        String updatedDeptName = "팡공";

        DeptDto.DeptUpdateRequest updateRequestDto = DeptDto.DeptUpdateRequest.builder()
                            .deptName(updatedDeptName)
                            .build();

        HttpEntity<DeptDto.DeptUpdateRequest> httpEntity = new HttpEntity<>(updateRequestDto);

        //when
        ResponseEntity<Long> entity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Long.class);

        //then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isGreaterThan(0L);

    }

}
