package com.wip.bool.position.service;

import com.wip.bool.cmmn.position.PositionFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.position.domain.Position;
import com.wip.bool.position.domain.PositionRepository;
import com.wip.bool.position.dto.PositionDto;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

    @InjectMocks
    private PositionService positionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PositionRepository positionRepository;

    private Position getPosition(String positionName, long id) {
        Position position = PositionFactory.getPosition(positionName);
        ReflectionTestUtils.setField(position, "id", id);
        return position;
    }

    @DisplayName("직위 추가")
    @Test
    public void 직위_추가_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        Position position = PositionFactory.getPosition(1L);
        String positionName = "리더";

        PositionDto.PositionSaveRequest requestDto = new PositionDto.PositionSaveRequest();
        ReflectionTestUtils.setField(requestDto, "positionName", positionName);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(position).when(positionRepository).save(any(Position.class));
        Long id = positionService.savePosition(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(position.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(positionRepository, times(1)).save(any(Position.class));
    }

    @DisplayName("직위 수정")
    @Test
    public void 직위_수정_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        Position position = PositionFactory.getPosition(1L);
        String positionName = "리더";

        PositionDto.PositionUpdateRequest requestDto = new PositionDto.PositionUpdateRequest();
        ReflectionTestUtils.setField(requestDto, "positionName", positionName);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(position)).when(positionRepository).findById(anyLong());
        Long id = positionService.updatePosition(user.getId(), position.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(position.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(positionRepository, times(1)).findById(anyLong());

    }

    @DisplayName("직위 삭제")
    @Test
    public void 직위_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        Position position = PositionFactory.getPosition(1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(position)).when(positionRepository).findById(anyLong());
        doNothing().when(positionRepository).delete(any(Position.class));
        Long resValue = positionService.deletePosition(user.getId(), position.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(positionRepository, times(1)).findById(anyLong());
        verify(positionRepository, times(1)).delete(any(Position.class));
    }

    @DisplayName("직위 리스트 조회")
    @Test
    public void 직위_리스트_조회_Service() throws Exception {

        //given
        List<Position> positions = PositionFactory.getPositionsWithId();

        //when
        doReturn(positions).when(positionRepository).findAll();
        List<PositionDto.PositionResponse> values = positionService.findAll();

        //then
        assertThat(values.size()).isEqualTo(positions.size());
        assertThat(values).extracting(PositionDto.PositionResponse::getPositionName).contains(PositionFactory.positionNames);

        //verify
        verify(positionRepository, times(1)).findAll();
    }

}
