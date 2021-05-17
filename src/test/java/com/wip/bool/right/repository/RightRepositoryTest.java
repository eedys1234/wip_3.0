package com.wip.bool.right.repository;

import com.wip.bool.cmmn.right.RightFactory;
import com.wip.bool.right.domain.Right;
import com.wip.bool.right.domain.RightRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RightRepositoryTest {

    @Autowired
    private RightRepository rightRepository;

    private Right getRight() {
        Right right = RightFactory.getRight();
        return right;
    }

    @DisplayName("권한 추가")
    @Test
    public void 권한_추가_Repository() throws Exception {

        //given
        Right right = getRight();

        //when
        Right addRight = rightRepository.save(right);

        //then
        assertThat(addRight.getId()).isGreaterThan(0L);
    }

    @DisplayName("권한 삭제")
    @Test
    public void 권한_삭제_Repository() throws Exception {

        //given
        Right right = getRight();
        Right addRight = rightRepository.save(right);

        //when
        rightRepository.delete(addRight);

        //then
        List<Right> rights = rightRepository.findAll();
        assertThat(rights.size()).isEqualTo(0);
    }
}
