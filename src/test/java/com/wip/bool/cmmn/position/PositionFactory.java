package com.wip.bool.cmmn.position;

import com.wip.bool.position.domain.Position;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PositionFactory {

    public static String[] positionNames = {"리더", "부장", "차장", "대리", "사원"};

    public static List<Position> getPositions() {
        return Arrays.stream(positionNames)
                .map(positionName -> getPosition(positionName))
                .collect(Collectors.toList());
    }

    public static List<Position> getPositionsWithId() {
        AtomicInteger index = new AtomicInteger(1);
        return Arrays.stream(positionNames)
                .map(positionName -> {
                    Position position = getPosition(positionName, index.longValue());
                    index.incrementAndGet();
                    return position;
                })
                .collect(Collectors.toList());
    }

    public static Position getPosition() {
        Position position = getPosition("사원");
        return position;
    }

    public static Position getPosition(long id) {
        Position position = getPosition("사원");
        ReflectionTestUtils.setField(position, "id", id);
        return position;
    }

    public static Position getPosition(String positionName) {
        Position position = Position.builder().positionName(positionName).build();
        return position;
    }

    public static Position getPosition(String positionName, long id) {
        Position position = Position.builder().positionName(positionName).build();
        ReflectionTestUtils.setField(position, "id", id);
        return position;
    }
}
