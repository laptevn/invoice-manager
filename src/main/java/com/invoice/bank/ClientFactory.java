package com.invoice.bank;

import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientFactory {
    @Bean
    public Client create(@Value("${bank.url}") String bankUrl) {
        return Feign.builder()
                .decoder(new GsonDecoder())
                .target(Client.class, bankUrl);
    }
}