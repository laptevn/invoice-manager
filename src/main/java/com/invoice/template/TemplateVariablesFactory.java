package com.invoice.template;

import com.invoice.WorkingDatesCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class TemplateVariablesFactory {
    private final static String DATE_FORMAT = "“d” MMMM yyyy";
    private final static DateTimeFormatter US_DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private final static DateTimeFormatter RU_DATE_FORMAT = DateTimeFormatter.ofPattern(
            DATE_FORMAT, WorkingDatesCalculator.RUSSIAN_LOCALE);

    private final Logger logger = LoggerFactory.getLogger(TemplateVariablesFactory.class);

    private final String paymentInUsd;
    private final WorkingDatesCalculator workingDatesCalculator;

    public TemplateVariablesFactory(
            @Value("${paymentInUsd}") String paymentInUsd, WorkingDatesCalculator workingDatesCalculator) {

        this.paymentInUsd = paymentInUsd;
        this.workingDatesCalculator = workingDatesCalculator;
    }

    public Map<String, String> create(LocalDate date, String paymentInRubles) {
        Objects.requireNonNull(date);
        logger.info(
                "Creating variables for {} date, {} payment in USD and {} payment in rubles",
                date,
                paymentInUsd,
                paymentInRubles);

        Map<String, String> variables = new HashMap<>();
        variables.put("DATE_US", date.format(US_DATE_FORMAT));
        variables.put("DATE_RU", date.format(RU_DATE_FORMAT));
        variables.put("PERIOD_US", workingDatesCalculator.getWorkingWeek(date, true));
        variables.put("PERIOD_RU", workingDatesCalculator.getWorkingWeek(date, false));
        variables.put("PAYMENT_US", paymentInUsd);
        variables.put("PAYMENT_RU", paymentInRubles);

        logger.info("Created variables {}", variables);
        return variables;
    }
}