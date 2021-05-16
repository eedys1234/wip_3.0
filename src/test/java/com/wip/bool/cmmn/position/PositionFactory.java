package com.wip.bool.cmmn.position;

import com.wip.bool.position.domain.Position;

public class PositionFactory {

    public static Position getPosition() {
        Position position = Position.builder().positionName("사원").build();
        return position;
    }

    public static Position getPosition(String positionName) {
        Position position = Position.builder().positionName(positionName).build();
        return position;
    }
}
