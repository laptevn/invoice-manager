package com.invoice;

import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class WorkingDatesCalculator {
    public final static Locale RUSSIAN_LOCALE = new Locale("ru");
    private final static int PAYMENT_CYCLE_SIZE_IN_WEEKS = 2;

    private final static String DATE_FORMAT_UNQOTED = "d MMMM yyyy";
    private final static DateTimeFormatter US_DATE_UNQOTED_FORMAT = DateTimeFormatter.ofPattern(DATE_FORMAT_UNQOTED);
    private final static DateTimeFormatter RU_DATE_UNQOTED_FORMAT = DateTimeFormatter.ofPattern(
            DATE_FORMAT_UNQOTED, RUSSIAN_LOCALE);

    private final static String DATE_WITHOUT_DAY_FORMAT = "MMMM yyyy";
    private final static DateTimeFormatter US_DATE_WITHOUT_DAY_FORMAT = DateTimeFormatter.ofPattern(DATE_WITHOUT_DAY_FORMAT);
    private final static DateTimeFormatter RU_DATE_WITHOUT_DAY_FORMAT = DateTimeFormatter.ofPattern(
            DATE_WITHOUT_DAY_FORMAT, RUSSIAN_LOCALE);

    private final static String DATE_WITHOUT_YEAR_FORMAT = "d MMMM";
    private final static DateTimeFormatter US_DATE_WITHOUT_YEAR_FORMAT = DateTimeFormatter.ofPattern(DATE_WITHOUT_YEAR_FORMAT);
    private final static DateTimeFormatter RU_DATE_WITHOUT_YEAR_FORMAT = DateTimeFormatter.ofPattern(
            DATE_WITHOUT_YEAR_FORMAT, RUSSIAN_LOCALE);

    private final static String SAME_MONTH_PERIOD_FORMAT = "%d-%d %s";
    private final static String DIFFERENT_MONTHS_PERIOD_FORMAT = "%s - %s %d";
    private final static String DIFFERENT_YEARS_PERIOD_FORMAT = "%s - %s";
    private final static String DAYS_PERIOD_FORMAT = "%d-%d";

    public String getWorkingWeek(LocalDate paymentDay, boolean isUsLocale) {
        LocalDate fixedDate = paymentDay.minusWeeks(PAYMENT_CYCLE_SIZE_IN_WEEKS);
        LocalDate firstDay = fixedDate.with(DayOfWeek.MONDAY);
        LocalDate lastDay = fixedDate.with(DayOfWeek.SUNDAY);

        if (firstDay.getMonthValue() == lastDay.getMonthValue()) {
            DateTimeFormatter dateFormat = isUsLocale ? US_DATE_WITHOUT_DAY_FORMAT : RU_DATE_WITHOUT_DAY_FORMAT;
            return String.format(SAME_MONTH_PERIOD_FORMAT, firstDay.getDayOfMonth(), lastDay.getDayOfMonth(), fixedDate.format(dateFormat));
        }

        if (firstDay.getYear() == lastDay.getYear()) {
            DateTimeFormatter dateFormat = isUsLocale ? US_DATE_WITHOUT_YEAR_FORMAT : RU_DATE_WITHOUT_YEAR_FORMAT;
            return String.format(DIFFERENT_MONTHS_PERIOD_FORMAT, firstDay.format(dateFormat), lastDay.format(dateFormat), fixedDate.getYear());
        }

        DateTimeFormatter dateFormat = isUsLocale ? US_DATE_UNQOTED_FORMAT : RU_DATE_UNQOTED_FORMAT;
        return String.format(DIFFERENT_YEARS_PERIOD_FORMAT, firstDay.format(dateFormat), lastDay.format(dateFormat));
    }

    public String getDaysOfWorkingWeek(LocalDate paymentDay) {
        LocalDate fixedDate = paymentDay.minusWeeks(PAYMENT_CYCLE_SIZE_IN_WEEKS);
        LocalDate firstDay = fixedDate.with(DayOfWeek.MONDAY);
        LocalDate lastDay = fixedDate.with(DayOfWeek.SUNDAY);
        return String.format(DAYS_PERIOD_FORMAT, firstDay.getDayOfMonth(), lastDay.getDayOfMonth());
    }
}