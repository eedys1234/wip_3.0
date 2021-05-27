package com.wip.bool.music.guitar.service;

import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.cmmn.user.UserFactory;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import com.wip.bool.music.guitar.dto.GuitarCodeDto;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuitarCodeServiceTest {

    @InjectMocks
    private GuitarCodeService guitarCodeService;

    @Mock
    private GuitarCodeRepository guitarCodeRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("기타코드 추가")
    @Test
    public void 기타코드_추가_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        GuitarCodeDto.GuitarCodeSaveRequest requestDto = new GuitarCodeDto.GuitarCodeSaveRequest();
        ReflectionTestUtils.setField(requestDto, "code", "B");

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(guitarCode).when(guitarCodeRepository).save(any(GuitarCode.class));
        doReturn(1).when(guitarCodeRepository).maxOrder();
        Long id = guitarCodeService.saveGuitarCode(user.getId(), requestDto);

        //then
        assertThat(id).isEqualTo(guitarCode.getId());

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(guitarCodeRepository, times(1)).save(any(GuitarCode.class));
        verify(guitarCodeRepository, times(1)).maxOrder();
    }

    @DisplayName("기타코드 리스트 조회")
    @Test
    public void 기타코드_리스트_조회_Service() throws Exception {

        //given
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);
        List<GuitarCode> guitarCodes = new ArrayList<>();
        guitarCodes.add(guitarCode);

        //when
        doReturn(guitarCodes).when(guitarCodeRepository).findAll();
        List<GuitarCodeDto.GuitarCodeResponse> values = guitarCodeService.getGuitarCodes();

        //then
        assertThat(values.size()).isEqualTo(guitarCodes.size());
        assertThat(values.get(0).getGuitarCodeId()).isEqualTo(guitarCodes.get(0).getId());
        
        
        //verify
        verify(guitarCodeRepository, times(1)).findAll();
    }

    @DisplayName("기타코드 삭제")
    @Test
    public void 기타코드_삭제_Service() throws Exception {

        //given
        User user = UserFactory.getAdminUser(1L);
        GuitarCode guitarCode = GuitarCodeFactory.getGuitarCode(1L);

        //when
        doReturn(Optional.ofNullable(user)).when(userRepository).findById(anyLong());
        doReturn(Optional.ofNullable(guitarCode)).when(guitarCodeRepository).findById(anyLong());
        doReturn(1L).when(guitarCodeRepository).delete(any(GuitarCode.class));
        Long resValue = guitarCodeService.deleteGuitarCode(user.getId(), guitarCode.getId());

        //then
        assertThat(resValue).isEqualTo(1L);

        //verify
        verify(userRepository, times(1)).findById(anyLong());
        verify(guitarCodeRepository, times(1)).findById(anyLong());
        verify(guitarCodeRepository, times(1)).delete(any(GuitarCode.class));
    }

}
