package com.wip.bool.calendar.controller;

import com.wip.bool.calendar.dto.CalendarDto;
import com.wip.bool.calendar.service.CalendarService;
import com.wip.bool.cmmn.ApiResponse;
import com.wip.bool.security.Permission;
import com.wip.bool.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class CalendarController {

    private final CalendarService calendarService;

    @Permission(target = Role.ROLE_NORMAL)
    @PostMapping(value = "/calendar")
    public ResponseEntity<ApiResponse<Long>> saveCalendar(@Valid @RequestBody CalendarDto.CalendarSaveRequest requestDto,
                                                          @RequestHeader("userId") Long userId,
                                                          Errors errors,
                                                          UriComponentsBuilder uriComponentsBuilder) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = calendarService.saveCalendar(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(ApiResponse.of(HttpStatus.CREATED.value(), id), httpHeaders, HttpStatus.CREATED);
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/calendar-dept")
    public ResponseEntity<ApiResponse<List<CalendarDto.CalendarResponse>>> getDeptCalendars(@RequestHeader("userId") Long userId,
                                                                                            @RequestParam Long from,
                                                                                            @RequestParam Long to) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), calendarService.getDeptCalendars(userId, from, to)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @GetMapping(value = "/calendar-individual")
    public ResponseEntity<ApiResponse<List<CalendarDto.CalendarResponse>>> getIndividualCalendar(@RequestHeader("userId") Long userId,
                                                                                                 @RequestParam Long from,
                                                                                                 @RequestParam Long to) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), calendarService.getIndividualCalenders(userId, from, to)));
    }

    @Permission(target = Role.ROLE_NORMAL)
    @DeleteMapping(value = "/calendar/{calendarId:[\\d]+}")
    public ResponseEntity<ApiResponse<Long>> deleteCalendar(@PathVariable("calendarId") Long calendarId,
                                                            @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(),calendarService.deleteCalendar(userId, calendarId)));
    }
}
