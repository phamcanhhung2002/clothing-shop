package com.clothingshop.api.domain.enums;

import lombok.Getter;
import java.time.temporal.ChronoUnit;

@Getter
public enum Interval {
    hour("%Y-%m-%dT%H", "yyyy-MM-dd'T'HH':00:00'", ":00:00", "HH", ChronoUnit.HOURS),
    day("%Y-%m-%d", "yyyy-MM-dd'T00:00:00'", "T00:00:00", "dd", ChronoUnit.DAYS),
    month("%Y-%m", "yyyy-MM'-01T00:00:00'", "-01T00:00:00", "MM", ChronoUnit.MONTHS);

    final String databaseTimeFormat;
    final String toStringFormat;
    final String truncatedLocalDateTimeFormat;
    final String toLocalTimeFormat;
    final ChronoUnit chronoUnit;

    private Interval(String databaseTimeFormat, String truncatedLocalDateTimeFormat, String toLocalDateTimeFormat,
            String toStringFormat, ChronoUnit chronoUnit) {
        this.databaseTimeFormat = databaseTimeFormat;
        this.truncatedLocalDateTimeFormat = truncatedLocalDateTimeFormat;
        this.toLocalTimeFormat = toLocalDateTimeFormat;
        this.toStringFormat = toStringFormat;
        this.chronoUnit = chronoUnit;
    }
}
