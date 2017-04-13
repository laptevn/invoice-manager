package com.invoice.bank;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Objects;

@Component
class PaymentConverter {
    private static final String NONCONVERSIBLE_PAYMENT = "0";

    public String convert(String payment) {
        Objects.requireNonNull(payment);
        if (payment.equals(NONCONVERSIBLE_PAYMENT)) {
            return NONCONVERSIBLE_PAYMENT;
        }

        return new BigDecimal(payment)
                .setScale(2, BigDecimal.ROUND_DOWN)
                .toPlainString();
    }
}