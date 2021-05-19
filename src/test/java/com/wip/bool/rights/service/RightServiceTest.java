package com.wip.bool.rights.service;

import com.wip.bool.cmmn.rights.RightsFactory;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepository;
import com.wip.bool.rights.dto.RightDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RightServiceTest {

    @InjectMocks
    private RightService rightService;

    @Mock
    private RightsRepository rightRepository;

    private Rights getRight() {
        Rights right = RightsFactory.getRights();
        ReflectionTestUtils.setField(right, "id", 1L);
        return right;
    }

    @DisplayName("권한 추가")
    @Test
    public void 권한_추가_Service() throws Exception {

        //given
        Rights right = getRight();
        RightDto.RightSaveRequest requestDto = new RightDto.RightSaveRequest();
        ReflectionTestUtils.setField(requestDto, "targetId", 1L);
        ReflectionTestUtils.setField(requestDto, "target", "USERBOX");
        ReflectionTestUtils.setField(requestDto, "authority", "GROUP");
        ReflectionTestUtils.setField(requestDto, "authorityId", 1L);

        //when
        doReturn(right).when(rightRepository).save(any(Rights.class));
        Long id = rightService.saveRight(requestDto);

        //then
        assertThat(id).isEqualTo(right.getId());

        //verify
        verify(rightRepository, times(1)).save(any(Rights.class));
    }

    @DisplayName("권한 삭제")
    @Test
    public void 권한_삭제_Service() throws Exception {

        //given
        Rights right = getRight();

        //when
        doReturn(Optional.ofNullable(right)).when(rightRepository).findById(any(Long.class));
        doNothing().when(rightRepository).delete(any(Rights.class));
        Long resValue = rightService.deleteRight(right.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(rightRepository, times(1)).findById(any(Long.class));
        verify(rightRepository, times(1)).delete(any(Rights.class));
    }
}
