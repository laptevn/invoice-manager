package com.invoice;

import com.invoice.configuration.InvoiceEmailConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
class InvoiceSender {
    private final Logger logger = LoggerFactory.getLogger(InvoiceSender.class);

    private final JavaMailSender mailSender;
    private final InvoiceEmailConfiguration invoiceEmailConfiguration;

    public InvoiceSender(JavaMailSender mailSender, InvoiceEmailConfiguration invoiceEmailConfiguration) {
        this.mailSender = mailSender;
        this.invoiceEmailConfiguration = invoiceEmailConfiguration;
    }

    public void send(File invoiceFile, String attachmentName) throws InvoiceException {
        logger.info("Sending an invoice to {}", invoiceEmailConfiguration.getAddressee());

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(invoiceEmailConfiguration.getAddressee());
            helper.setSubject(invoiceEmailConfiguration.getSubject());
            helper.setText(invoiceEmailConfiguration.getText());
            helper.addAttachment(attachmentName, invoiceFile);
        } catch (MessagingException e) {
            throw new InvoiceException("Cannot create a message with invoice", e);
        }

        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new InvoiceException("Cannot send an email", e);
        }

        logger.info("Sent an invoice to {}", invoiceEmailConfiguration.getAddressee());
    }
}