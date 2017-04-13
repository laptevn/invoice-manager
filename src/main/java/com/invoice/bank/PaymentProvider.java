package com.invoice.bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PaymentProvider {
    private final Logger logger = LoggerFactory.getLogger(PaymentProvider.class);
    private final Client client;
    private final PaymentConverter paymentConverter;

    public PaymentProvider(Client client, PaymentConverter paymentConverter) {
        this.client = client;
        this.paymentConverter = paymentConverter;
    }

    public String loadPayment(String accountId, String token, String fromDate) {
        logger.info("Loading payment from {}", fromDate);

        List<Operation> operations = client.getLatestOperation(accountId, token, fromDate);
        logger.info("Bank sent {}", operations);
        if (operations == null || operations.size() != 1) {
            throw new IllegalStateException("Bank didn't return payment information");
        }

        String result = paymentConverter.convert(operations.get(0).getAmount());
        logger.info("Loaded {} payment", result);
        return result;
    }
}