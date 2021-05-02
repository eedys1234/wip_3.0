package com.wip.bool.position;

import com.wip.bool.position.domain.Position;
import com.wip.bool.position.domain.PositionRepository;
import com.wip.bool.position.service.PositionService;
import com.wip.bool.position.dto.PositionDto;
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
public class PositionControllerTest {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private PositionService positionService;

    @After
    public void tearDown() throws Exception {
        positionRepository.deleteAll();
    }

    @Test
    public void 직위_추가() throws Exception {

        //given
        String positionName = "체장";

        PositionDto.PositionSaveRequest requestDto = PositionDto.PositionSaveRequest.builder()
                                                                .positionName(positionName)
                                                                .build();

        String url = "http://localhost:" + port + "/api/v1/position";

        //when
        ResponseEntity<Long> entity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(entity.getBody()).isGreaterThan(0L);
    }

    @Test
    public void 직위_수정() throws Exception {

        //given
        String positionName = "체장";

        Position position = Position.builder().positionName(positionName).build();
        Long id = positionRepository.save(position).getId();

        String updatedPositionName = "부체장";

        String url = "http://localhost:" + port + "/api/v1/position/" + id;
        PositionDto.PositionUpdateRequest requestDto = PositionDto.PositionUpdateRequest.builder()
                                                                                        .positionName(positionName)
                                                                                        .build();

        //when
        HttpEntity<PositionDto.PositionUpdateRequest> httpEntity = new HttpEntity<>(requestDto);
        ResponseEntity<Long> entity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Long.class);

        //then
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isGreaterThan(0L);
    }

}
