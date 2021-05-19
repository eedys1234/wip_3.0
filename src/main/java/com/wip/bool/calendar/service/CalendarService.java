package com.wip.bool.calendar.service;

import com.wip.bool.calendar.repository.Calendar;
import com.wip.bool.calendar.repository.CalendarRepository;
import com.wip.bool.calendar.dto.CalendarDto;
import com.wip.bool.cmmn.type.ShareType;
import com.wip.bool.exception.excp.EntityNotFoundException;
import com.wip.bool.exception.excp.ErrorCode;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public Long save(Long userId, CalendarDto.CalendarSaveRequest requestDto) {

        User user = selectUser(userId);

        LocalDateTime calendarDate = new Timestamp(requestDto.getCalendarDate()).toLocalDateTime();
        ShareType shareType = ShareType.valueOf(requestDto.getShareType());

        Calendar calendar = Calendar.createCalender(requestDto.getTitle(), requestDto.getContent(),
                calendarDate, shareType, user);

        return calendarRepository.save(calendar).getId();
    }

    @Transactional(readOnly = true)
    public List<CalendarDto.CalendarResponse> getDeptCalendars(Long userId, Long from, Long to) {

        User user = userRepository.deptByUser(userId)
                .orElseThrow(()-> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));

        if(isGreaterOneMonth(from, to)) {
            throw new IllegalArgumentException("조회시작일자와 마지막일자가 한달이상 차이납니다.");
        }

        List<Long> userIds = userRepository.usersByDept(user.getDept().getId());

        LocalDateTime fromDate = new Timestamp(from).toLocalDateTime();
        LocalDateTime toDate = new Timestamp(to).toLocalDateTime();

        return calendarRepository.deptCalendars(userIds, fromDate, toDate)
                .stream()
                .filter(calendarResponse -> calendarResponse.getShareType().equals(ShareType.DEPT) ||
                        calendarResponse.getShareType().equals(ShareType.PUBLIC))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalendarDto.CalendarResponse> getIndividualCalenders(Long userId, long from, long to) {

        selectUser(userId);

        if(isGreaterOneMonth(from, to)) {
            throw new IllegalArgumentException("조회시작일자와 마지막일자가 한달이상 차이납니다.");
        }

        LocalDateTime fromDate = new Timestamp(from).toLocalDateTime();
        LocalDateTime toDate = new Timestamp(to).toLocalDateTime();

        return calendarRepository.individualCalendars(userId, fromDate, toDate)
                .stream()
                .filter(calendarResponse -> calendarResponse.getShareType().equals(ShareType.PRIVATE))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long userId, Long calendarId) {

        Calendar calendar = calendarRepository.findByIdAndUserId(userId, calendarId)
                .orElseThrow(() -> new EntityNotFoundException(calendarId, ErrorCode.NOT_FOUND_CALENDAR));

        return calendarRepository.delete(calendar);
    }

    private User selectUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException(userId, ErrorCode.NOT_FOUND_USER));
    }

    private boolean isGreaterOneMonth(long from, long to) {

        LocalDate fromDate = new Timestamp(from).toLocalDateTime().toLocalDate();
        LocalDate toDate = new Timestamp(to).toLocalDateTime().toLocalDate();

        Period period = Period.between(fromDate, toDate);

        int year = period.getYears();
        int month = period.getMonths();
        int day = period.getDays();

        if(year >= 1 || (month >= 1 && day >= 1)) {
            return true;
        }

        return false;
    }

}
