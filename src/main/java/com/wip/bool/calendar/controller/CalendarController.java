package com.wip.bool.calendar.controller;

import com.wip.bool.calendar.service.CalendarService;
import com.wip.bool.calendar.dto.CalendarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Validated
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping(value = "/calendar")
    public ResponseEntity<Long> save(@Valid @RequestBody CalendarDto.CalendarSaveRequest requestDto,
                                    @RequestHeader("userId") Long userId,
                                    Errors errors, UriComponentsBuilder uriComponentsBuilder)
    {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Long id = calendarService.save(userId, requestDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("{id}").buildAndExpand(id).toUri());

        return new ResponseEntity<>(id, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/calendar/dept")
    public ResponseEntity<List<CalendarDto.CalendarResponse>> getDeptCalendars(
            @RequestHeader("userId") @Positive Long userId,
            @RequestParam("from") @Positive Long from,
            @RequestParam("to") @Positive Long to) {

        return ResponseEntity.ok(calendarService.getDeptCalendars(userId, from, to));
    }

    @GetMapping(value = "/calendar/individual")
    public ResponseEntity<List<CalendarDto.CalendarResponse>> getIndividualCalendar(
            @RequestHeader("userId") @Positive Long userId,
            @RequestParam("from") @Positive Long from,
            @RequestParam("to") @Positive Long to
    ) {

        return ResponseEntity.ok(calendarService.getIndividualCalenders(userId, from, to));
    }

    @DeleteMapping(value = "/calendar/{calendarId:[\\d]+}")
    public ResponseEntity<Long> delete(@PathVariable("calendarId") Long calendarId) {

        return ResponseEntity.ok(calendarService.delete(calendarId));
    }
}
