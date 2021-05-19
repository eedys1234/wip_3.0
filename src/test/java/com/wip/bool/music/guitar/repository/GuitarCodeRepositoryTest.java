package com.wip.bool.music.guitar.repository;

import com.wip.bool.cmmn.music.guitarcode.GuitarCodeFactory;
import com.wip.bool.configure.TestConfig;
import com.wip.bool.music.guitar.domain.GuitarCode;
import com.wip.bool.music.guitar.domain.GuitarCodeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@Transactional
public class GuitarCodeRepositoryTest {

    @Autowired
    private GuitarCodeRepository guitarCodeRepository;

    private GuitarCode getGuitarCode() {
        return GuitarCodeFactory.getGuitarCode();
    }

    @DisplayName("기타코드 추가")
    @Test
    public void 기타코드_추가_Repository() throws Exception {

        //given
        GuitarCode guitarCode = getGuitarCode();

        //when
        GuitarCode addGuitarCode = guitarCodeRepository.save(guitarCode);

        //then
        assertThat(addGuitarCode.getId()).isGreaterThan(0L);
        assertThat(addGuitarCode.getCode()).isEqualTo(guitarCode.getCode());
        assertThat(addGuitarCode.getGuitarOrder()).isEqualTo(guitarCode.getGuitarOrder());
    }

    @DisplayName("기타코드 조회")
    @Test
    public void 기타코드_조회_Repository() throws Exception {

        //given
        GuitarCode guitarCode = getGuitarCode();
        GuitarCode addGuitarCode = guitarCodeRepository.save(guitarCode);

        //when
        GuitarCode findGuitarCode = guitarCodeRepository.findById(addGuitarCode.getId())
                .orElseThrow(() -> new IllegalArgumentException(""));

        //then
        assertThat(findGuitarCode.getId()).isEqualTo(addGuitarCode.getId());
        assertThat(findGuitarCode.getCode()).isEqualTo(addGuitarCode.getCode());
        assertThat(findGuitarCode.getGuitarOrder()).isEqualTo(addGuitarCode.getGuitarOrder());
    }

    @DisplayName("기타코드 리스트 조회")
    @Test
    public void 기타코드_리스트_조회_Repository() throws Exception {

        //given
        GuitarCode guitarCode = getGuitarCode();
        GuitarCode addGuitarCode = guitarCodeRepository.save(guitarCode);

        //when
        List<GuitarCode> guitarCodes = guitarCodeRepository.findAll();

        //then
        assertThat(guitarCodes.get(0).getId()).isEqualTo(addGuitarCode.getId());
        assertThat(guitarCodes.get(0).getCode()).isEqualTo(addGuitarCode.getCode());
        assertThat(guitarCodes.get(0).getGuitarOrder()).isEqualTo(addGuitarCode.getGuitarOrder());
    }

    @DisplayName("기타코드 삭제")
    @Test
    public void 기타코드_삭제_Repository() throws Exception {

        //given
        GuitarCode guitarCode = getGuitarCode();
        GuitarCode addGuitarCode = guitarCodeRepository.save(guitarCode);

        //when
        Long resValue = guitarCodeRepository.delete(addGuitarCode);

        //then
        assertThat(resValue).isEqualTo(1L);
    }
}
