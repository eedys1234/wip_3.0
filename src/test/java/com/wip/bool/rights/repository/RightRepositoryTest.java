package com.wip.bool.rights.repository;

import com.wip.bool.cmmn.rights.RightsFactory;
import com.wip.bool.rights.domain.Rights;
import com.wip.bool.rights.domain.RightsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RightRepositoryTest {

    @Autowired
    private RightsRepository rightRepository;

    private Rights getRights() {
        Rights rights = RightsFactory.getRights();
        return rights;
    }

    @DisplayName("권한 추가")
    @Test
    public void 권한_추가_Repository() throws Exception {

        //given
        Rights right = getRights();

        //when
        Rights addRight = rightRepository.save(right);

        //then
        assertThat(addRight.getId()).isGreaterThan(0L);
    }

    @DisplayName("권한 삭제")
    @Test
    public void 권한_삭제_Repository() throws Exception {

        //given
        Rights right = getRights();
        Rights addRight = rightRepository.save(right);

        //when
        rightRepository.delete(addRight);

        //then
        List<Rights> rights = rightRepository.findAll();
        assertThat(rights.size()).isEqualTo(0);
    }
}
