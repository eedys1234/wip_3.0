package com.wip.bool.position.repository;

import com.wip.bool.cmmn.position.PositionFactory;
import com.wip.bool.position.domain.Position;
import com.wip.bool.position.domain.PositionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PositionRepositoryTest {

    @Autowired
    private PositionRepository positionRepository;

    private Position getPosition() {
        Position position = PositionFactory.getPosition();
        return position;
    }

    private Position getPosition(String positionName) {
        Position position = PositionFactory.getPosition(positionName);
        return position;
    }

    @DisplayName("직위 추가")
    @Test
    public void 직위_추가_Repository() throws Exception {

        //given
        Position position = getPosition();

        //when
        Position addPosition = positionRepository.save(position);

        //then
        assertThat(addPosition.getId()).isGreaterThan(0L);
    }

    @DisplayName("직위 수정")
    @Test
    public void 직위_수정_Repository() throws Exception {

        //given
        Position position = getPosition();
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
        Position position = getPosition();
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
        String[] positionNames = {"리더", "부장", "차장", "대리", "사원"};
        List<Position> positions = new ArrayList<>();

        for(int i=0;i<positionNames.length;i++)
        {
            Position position = getPosition(positionNames[i]);
            positions.add(positionRepository.save(position));
        }

        //when
        List<Position> values = positionRepository.findAll();

        //then
        assertThat(values.size()).isEqualTo(positionNames.length);
        assertThat(values).extracting(Position::getPositionName).contains(positionNames);
    }
}
