package com.invoice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "invoiceEmail")
public class InvoiceEmailConfiguration {
    private String addressee;
    private String subject;
    private String text;

    public String getAddressee() {
        return addressee;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }
}