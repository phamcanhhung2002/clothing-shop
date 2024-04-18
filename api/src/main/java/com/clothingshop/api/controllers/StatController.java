package com.clothingshop.api.controllers;

import com.clothingshop.api.domain.dtos.OrderStatResponseDto;
import com.clothingshop.api.domain.enums.Interval;
import com.clothingshop.api.domain.enums.TimeZoneSign;
import com.clothingshop.api.services.StatService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/stat")
@Log
public class StatController {
    @Autowired
    StatService statService;

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderStatResponseDto>> getOrderStat(
            @RequestParam String fromDateFormat,
            @RequestParam String toDateFormat,
            @RequestParam String timeZoneOffset,
            @RequestParam TimeZoneSign timeZoneSign,
            @RequestParam(required = false) Long userId,
            @RequestParam Interval intervalType) {
        String timeZone = timeZoneSign.getLabel() + timeZoneOffset;
        log.info("timeZone " + timeZone);
        Date fromDate = getFromDateFromTimestamp(fromDateFormat, timeZone);
        Date toDate = getToDateFromTimestamp(toDateFormat, timeZone);

        if (fromDate.compareTo(toDate) > 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.info("fromDate " + fromDate);
        log.info("toDate " + toDate);
        return new ResponseEntity<>(statService.getOrderStat(fromDate, toDate, timeZone, userId, intervalType),
                HttpStatus.OK);
    }

    @GetMapping("/seed")
    public String seed() throws Exception {
        statService.seed();

        return "Seed successfully!";
    }

    Date getFromDateFromTimestamp(String formatFromDate, String timeZone) {
        LocalDate localDate = LocalDate.parse(formatFromDate);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        ZonedDateTime utcZonedDateTime = startOfDay.atZone(ZoneId.of(timeZone));
        Instant instant = utcZonedDateTime.toInstant();
        return Date.from(instant);
    }

    Date getToDateFromTimestamp(String formatToDate, String timeZone) {
        LocalDate localDate = LocalDate.parse(formatToDate);
        LocalDateTime endOfDay = LocalTime.MAX.atDate(localDate);
        ZonedDateTime utcZonedDateTime = endOfDay.atZone(ZoneId.of(timeZone));
        Instant instant = utcZonedDateTime.toInstant();
        return Date.from(instant);
    }
}
