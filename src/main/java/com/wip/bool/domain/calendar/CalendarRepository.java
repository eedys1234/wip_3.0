package com.wip.bool.domain.calendar;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.web.dto.calendar.CalendarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.domain.calendar.QCalendar.calendar;
import static com.wip.bool.domain.dept.QDept.dept;
import static com.wip.bool.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class CalendarRepository {

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public Calendar save(Calendar calendar) {
        entityManager.persist(calendar);
        return calendar;
    }

    public Optional<Calendar> findById(Long calendarId) {
        return Optional.ofNullable(entityManager.find(Calendar.class, calendarId));
    }

    public List<CalendarDto.CalendarResponse> deptCalendars(Long userId, List<LocalDateTime> periodDate) {

        return queryFactory.select(Projections.constructor(CalendarDto.CalendarResponse.class,
                calendar.id, calendar.title, calendar.content, calendar.calendarDate, calendar.shareType, user.email, user.id))
                .from(user)
                .innerJoin(dept)
                .on(user.dept.eq(dept))
                .innerJoin(calendar)
                .on(user.eq(calendar.user))
                .where(user.id.eq(userId), calendar.calendarDate.in(periodDate))
                .fetch();
    }

    public List<CalendarDto.CalendarResponse> individualCalendars(Long userId, List<LocalDateTime> periodDate) {

        return queryFactory.select(Projections.constructor(CalendarDto.CalendarResponse.class,
                calendar.id, calendar.title, calendar.content, calendar.calendarDate, calendar.shareType, user.email, user.id))
                .from(calendar)
                .innerJoin(user)
                .on(calendar.user.eq(user))
                .where(calendar.user.id.eq(userId), calendar.calendarDate.in(periodDate))
                .fetch();
    }

    public Long delete(Calendar calendar) {
        entityManager.remove(calendar);
        return 1L;
    }

}
