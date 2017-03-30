package com.invoice;

import static org.junit.Assert.assertEquals;

import com.invoice.template.TemplateVariablesFactory;
import org.junit.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TemplateVariablesFactoryTest {
    private final static String PAYMENT_IN_USD = "1000";
    private final static String PAYMENT_IN_RUBLES = "32000";
    private final static String DATE_US = "DATE_US";
    private final static String DATE_RU = "DATE_RU";
    private final static String PERIOD_US = "PERIOD_US";
    private final static String PERIOD_RU = "PERIOD_RU";
    private final static String PAYMENT_US = "PAYMENT_US";
    private final static String PAYMENT_RU = "PAYMENT_RU";

    @Test(expected = NullPointerException.class)
    public void nullDate() {
        createVariablesFactory().create(null, "");
    }

    @Test
    public void singleMonth() {
        TemplateVariablesFactory factory = createVariablesFactory();
        LocalDate date = LocalDate.parse("2017-03-22", DateTimeFormatter.ISO_LOCAL_DATE);
        Map<String, String> variables = factory.create(date, PAYMENT_IN_RUBLES);
        assertEquals("Invalid US date", "“22” March 2017", variables.get(DATE_US));
        assertEquals("Invalid RU date", "“22” марта 2017", variables.get(DATE_RU));
        assertEquals("Invalid US period", "6-12 March 2017", variables.get(PERIOD_US));
        assertEquals("Invalid RU period", "6-12 марта 2017", variables.get(PERIOD_RU));
        assertEquals("Invalid US payment", PAYMENT_IN_USD, variables.get(PAYMENT_US));
        assertEquals("Invalid RU payment", PAYMENT_IN_RUBLES, variables.get(PAYMENT_RU));
    }

    @Test
    public void differentMonthsBetweenPaymentDayAndWorkingPeriod() {
        TemplateVariablesFactory factory = createVariablesFactory();
        LocalDate date = LocalDate.parse("2017-03-01", DateTimeFormatter.ISO_LOCAL_DATE);
        Map<String, String> variables = factory.create(date, PAYMENT_IN_RUBLES);
        assertEquals("Invalid US date", "“1” March 2017", variables.get(DATE_US));
        assertEquals("Invalid RU date", "“1” марта 2017", variables.get(DATE_RU));
        assertEquals("Invalid US period", "13-19 February 2017", variables.get(PERIOD_US));
        assertEquals("Invalid RU period", "13-19 февраля 2017", variables.get(PERIOD_RU));
        assertEquals("Invalid US payment", PAYMENT_IN_USD, variables.get(PAYMENT_US));
        assertEquals("Invalid RU payment", PAYMENT_IN_RUBLES, variables.get(PAYMENT_RU));
    }

    @Test
    public void differentMonthsInWorkingPeriod() {
        TemplateVariablesFactory factory = createVariablesFactory();
        LocalDate date = LocalDate.parse("2017-03-15", DateTimeFormatter.ISO_LOCAL_DATE);
        Map<String, String> variables = factory.create(date, PAYMENT_IN_RUBLES);
        assertEquals("Invalid US date", "“15” March 2017", variables.get(DATE_US));
        assertEquals("Invalid RU date", "“15” марта 2017", variables.get(DATE_RU));
        assertEquals("Invalid US period", "27 February - 5 March 2017", variables.get(PERIOD_US));
        assertEquals("Invalid RU period", "27 февраля - 5 марта 2017", variables.get(PERIOD_RU));
        assertEquals("Invalid US payment", PAYMENT_IN_USD, variables.get(PAYMENT_US));
        assertEquals("Invalid RU payment", PAYMENT_IN_RUBLES, variables.get(PAYMENT_RU));
    }

    @Test
    public void differentYearsInWorkingPeriod() {
        TemplateVariablesFactory factory = createVariablesFactory();
        LocalDate date = LocalDate.parse("2017-01-11", DateTimeFormatter.ISO_LOCAL_DATE);
        Map<String, String> variables = factory.create(date, PAYMENT_IN_RUBLES);
        assertEquals("Invalid US date", "“11” January 2017", variables.get(DATE_US));
        assertEquals("Invalid RU date", "“11” января 2017", variables.get(DATE_RU));
        assertEquals("Invalid US period", "26 December 2016 - 1 January 2017", variables.get(PERIOD_US));
        assertEquals("Invalid RU period", "26 декабря 2016 - 1 января 2017", variables.get(PERIOD_RU));
        assertEquals("Invalid US payment", PAYMENT_IN_USD, variables.get(PAYMENT_US));
        assertEquals("Invalid RU payment", PAYMENT_IN_RUBLES, variables.get(PAYMENT_RU));
    }

    private static TemplateVariablesFactory createVariablesFactory() {
        return new TemplateVariablesFactory(PAYMENT_IN_USD, new WorkingDatesCalculator());
    }
}