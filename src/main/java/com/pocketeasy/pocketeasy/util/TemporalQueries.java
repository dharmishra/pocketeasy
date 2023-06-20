package com.pocketeasy.pocketeasy.util;

import java.time.Instant;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAccessor;

public class TemporalQueries {

    public static Boolean isCurrentMonth(TemporalAccessor temporal) {
        Instant ref = Instant.now();
        return Month.from(temporal) == Month.from(ref) && Year.from(temporal).equals(Year.from(ref));
    }
}
