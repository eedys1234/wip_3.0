package com.wip.bool.calendar.domain;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wip.bool.calendar.dto.CalendarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.wip.bool.calendar.domain.QCalendar.calendar;
import static com.wip.bool.user.domain.QUser.user;

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

    public Optional<Calendar> findByIdAndUserId(Long userId, Long calendarId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(calendar)
                            .where(calendar.id.eq(calendarId), calendar.user.id.eq(userId))
                            .fetchOne()
        );
    }

    public List<CalendarDto.CalendarResponse> deptCalendars(List<Long> userIds, LocalDateTime fromDate, LocalDateTime toDate) {

        return queryFactory.select(Projections.constructor(CalendarDto.CalendarResponse.class,
                calendar.id, calendar.title, calendar.content, calendar.calendarDate, calendar.shareType, user.email, user.id))
                .from(calendar)
                .where(calendar.user.id.in(userIds), calendar.calendarDate.between(fromDate, toDate))
                .fetch();
    }

    public List<CalendarDto.CalendarResponse> individualCalendars(Long userId, LocalDateTime fromDate, LocalDateTime toDate) {

        return queryFactory.select(Projections.constructor(CalendarDto.CalendarResponse.class,
                calendar.id, calendar.title, calendar.content, calendar.calendarDate, calendar.shareType, user.email, user.id))
                .from(calendar)
                .innerJoin(user)
                .on(calendar.user.eq(user))
                .where(calendar.user.id.eq(userId), calendar.calendarDate.between(fromDate, toDate))
                .fetch();
    }

    public Long delete(Calendar calendar) {
        entityManager.remove(calendar);
        return 1L;
    }

}
