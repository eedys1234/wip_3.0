package com.wip.bool.calendar.service;

import com.wip.bool.calendar.domain.Calendar;
import com.wip.bool.calendar.domain.CalendarRepository;
import com.wip.bool.calendar.domain.ShareType;
import com.wip.bool.user.domain.User;
import com.wip.bool.user.domain.UserRepository;
import com.wip.bool.calendar.dto.CalendarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        selectUser(userId);

        List<LocalDateTime> periodDate = getPeriodDate(from, to);

        return calendarRepository.deptCalendars(userId, periodDate)
                .stream()
                .filter(calendarResponse -> calendarResponse.getShareType().equals(ShareType.DEPT) ||
                        calendarResponse.getShareType().equals(ShareType.PUBLIC))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalendarDto.CalendarResponse> getIndividualCalenders(Long userId, long from, long to) {

        selectUser(userId);

        List<LocalDateTime> periodDate = getPeriodDate(from, to);

        return calendarRepository.individualCalendars(userId, periodDate)
                .stream()
                .filter(calendarResponse -> calendarResponse.getShareType().equals(ShareType.PRIVATE))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long calendarId) {

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다. id = " + calendarId));

        return calendarRepository.delete(calendar);
    }

    private User selectUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("사용자가 존재하지 않습니다. id = " + userId));
    }

    private List<LocalDateTime> getPeriodDate(long from, long to) {

        LocalDateTime fromDate = new Timestamp(from).toLocalDateTime();
        LocalDateTime toDate = new Timestamp(to).toLocalDateTime();
        List<LocalDateTime> periodDate = new ArrayList<>();

        while(toDate.isAfter(fromDate)) {
            periodDate.add(fromDate);
            fromDate = fromDate.plusDays(1);
        }

        return periodDate;
    }
}
