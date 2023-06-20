package com.pocketeasy.pocketeasy.util;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.definition.FieldDefinition;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import org.springframework.scheduling.support.CronExpression;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cronutils.model.CronType.CRON4J;
import static com.cronutils.model.CronType.QUARTZ;

public class CronUtil {

    public static void main(String[] args) {
        var expression = CronExpression.parse("0 0 0 28 2 ?");


        LocalDate startDate = LocalDate.now();
        System.out.println("Before " + startDate);
        var endDate  = startDate.plusYears(2);
        System.out.println("After " + endDate);


//        Set<LocalDate> daysRange = Stream.iterate(startDate, date -> date.plusYears(2)).collect(Collectors.toSet());
//        System.out.println(daysRange);
        var result = expression.next(LocalDateTime.now());
        Set<LocalDate> cronDates = new LinkedHashSet<>();
        while (result.toLocalDate().isBefore(endDate)) {
            cronDates.add(result.toLocalDate());
            result = expression.next(result);


        }
        System.out.println(cronDates);



        String date = "1/13/2012";
        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yyyy"));
        convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));

        System.out.println(convertedDate);


        CronDefinition cronDefinition =
                CronDefinitionBuilder.defineCron()
                        .withSeconds().and()
                        .withMinutes().and()
                        .withHours().and()
                        .withDayOfMonth()
                        .supportsHash().supportsL().supportsW().and()
                        .withMonth().and()
//                        .withDayOfWeek()
//                        .withIntMapping(7, 0) //we support non-standard non-zero-based numbers!
//                        .supportsHash().supportsL().supportsW().and()
                        .withYear().and()
                        .instance();

        //cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CRON4J);

        CronParser parser = new CronParser(cronDefinition);

        // Get date for last execution
        ZonedDateTime now = ZonedDateTime.now();
        Cron parse = parser.parse("0 0 0 29 * *");

        Map<CronFieldName, FieldDefinition> cronFieldNameFieldDefinitionMap = parse.getCronDefinition().retrieveFieldDefinitionsAsMap();

        System.out.println("*****************");
        parse.retrieveFieldsAsMap().entrySet().stream().map(e -> e.getValue().getExpression().asString()).forEach(System.out::println);

        ExecutionTime executionTime = ExecutionTime.forCron(parse);



        ZonedDateTime lastExecution = executionTime.lastExecution(now).get();

        ZonedDateTime nextExecution = executionTime.nextExecution(now).get();
        System.out.println(nextExecution);

        Duration timeFromLastExecution = executionTime.timeFromLastExecution(now).get();
        System.out.println(timeFromLastExecution);

        Duration timeToNextExecution = executionTime.timeToNextExecution(now).get();

        System.out.println(timeToNextExecution);

    }

}
