package com.invoice;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WorkingDatesCalculatorTest {
    @Test
    public void daysForSameMonth() {
        String actualDays = new WorkingDatesCalculator().getDaysOfWorkingWeek(
                LocalDate.parse("2017-03-22", DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals("6-12", actualDays);
    }

    @Test
    public void daysForDifferentMonths() {
        String actualDays = new WorkingDatesCalculator().getDaysOfWorkingWeek(
                LocalDate.parse("2017-03-15", DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals("27-5", actualDays);
    }
}