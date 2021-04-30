package com.wip.bool.domain.calendar;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.web.dto.calendar.CalendarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CalendarRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public Long save(Calendar calendar) {
        entityManager.persist(calendar);
        return calendar.getId();
    }

    public List<CalendarDto.CalendarResponse> deptCalendars(Long userId, LocalDateTime fromDate,
                                                            LocalDateTime toDate) {


    }
}
