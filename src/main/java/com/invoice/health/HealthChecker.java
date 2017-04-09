package com.invoice.health;

import com.invoice.bank.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
class HealthChecker {
    private final Client client;
    private final String accountId;
    private final String token;

    public HealthChecker(
            Client client, @Value("${bank.accountId}") String accountId, @Value("${bank.token}") String token) {

        this.client = client;
        this.accountId = accountId;
        this.token = token;
    }

    public Health check() {
        Health.Builder builder = new Health.Builder();
        try {
            client.getLatestOperation(accountId, token, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            builder.up();
        } catch (Exception e) {
            builder.down(e);
        }

        return builder.build();
    }
}