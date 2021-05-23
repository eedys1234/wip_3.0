package com.wip.bool.position.repository;

import com.wip.bool.cmmn.position.PositionFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.position.domain.Position;
import com.wip.bool.position.domain.PositionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wip.bool.cmmn.util.WIPProperty.TEST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles(TEST)
@Transactional
public class PositionRepositoryTest {

    @Autowired
    private PositionRepository positionRepository;

    private Position getPosition(String positionName) {
        Position position = PositionFactory.getPosition(positionName);
        return position;
    }

    @DisplayName("직위 추가")
    @Test
    public void 직위_추가_Repository() throws Exception {

        //given
        Position position = PositionFactory.getPosition();

        //when
        Position addPosition = positionRepository.save(position);

        //then
        List<Position> positions = positionRepository.findAll();
        assertThat(addPosition.getId()).isGreaterThan(0L);
        assertThat(addPosition.getId()).isEqualTo(positions.get(0).getId());
        assertThat(addPosition.getPositionName()).isEqualTo(positions.get(0).getPositionName());
    }

    @DisplayName("직위 수정")
    @Test
    public void 직위_수정_Repository() throws Exception {

        //given
        Position position = PositionFactory.getPosition();
        String updatePositionName = "리더";
        Position addPosition = positionRepository.save(position);

        //when
        addPosition.updatePositionName(updatePositionName);

        //then
        List<Position> positions = positionRepository.findAll();
        assertThat(positions.get(0).getPositionName()).isEqualTo(updatePositionName);
    }

    @DisplayName("직위 삭제")
    @Test
    public void 직위_삭제_Repsoitory() throws Exception {

        //given
        Position position = PositionFactory.getPosition();
        Position addPosition = positionRepository.save(position);

        //when
        positionRepository.delete(addPosition);

        //then
        List<Position> positions = positionRepository.findAll();
        assertThat(positions.size()).isEqualTo(0);
    }

    @DisplayName("직위 리스트 조회")
    @Test
    public void 직위_리스트_조회_Repository() throws Exception {

        //given
        List<Position> positions = PositionFactory.getPositions();

        for(Position position : positions)
        {
            positionRepository.save(position);
        }

        //when
        List<Position> values = positionRepository.findAll();

        //then
        assertThat(values.size()).isEqualTo(positions.size());
        assertThat(values).extracting(Position::getPositionName).contains(PositionFactory.positionNames);
    }
}
