package com.invoice;

import com.invoice.bank.PaymentProvider;
import com.invoice.template.DocumentTemplateEditor;
import com.invoice.template.TemplateVariablesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InvoiceEngine {
    private final static String INVOICE_NAME_FORMAT = "Invoice %s.docx";

    private final Logger logger = LoggerFactory.getLogger(InvoiceEngine.class);
    private final DocumentTemplateEditor documentTemplateEditor;
    private final PaymentProvider paymentProvider;
    private final TemplateVariablesFactory templateVariablesFactory;
    private final WorkingDatesCalculator workingDatesCalculator;
    private final InvoiceSender invoiceSender;
    private final String accountId;
    private final String token;

    public InvoiceEngine(
            DocumentTemplateEditor documentTemplateEditor,
            PaymentProvider paymentProvider,
            TemplateVariablesFactory templateVariablesFactory,
            WorkingDatesCalculator workingDatesCalculator,
            InvoiceSender invoiceSender,
            @Value("${bank.accountId}") String accountId,
            @Value("${bank.token}") String token) {

        this.documentTemplateEditor = documentTemplateEditor;
        this.paymentProvider = paymentProvider;
        this.templateVariablesFactory = templateVariablesFactory;
        this.workingDatesCalculator = workingDatesCalculator;
        this.invoiceSender = invoiceSender;
        this.accountId = accountId;
        this.token = token;
    }

    @Scheduled(cron = "${invoiceCreationTime}")
    public void run() {
        logger.info("Starting preparing an invoice");

        try {
            doRun();
        } catch (InvoiceException e) {
            logger.error("Cannot prepare an invoice", e);
        }

        logger.info("Finished preparing an invoice");
    }

    private void doRun() throws InvoiceException {
        LocalDate date = LocalDate.now();
        String payment = paymentProvider.loadPayment(accountId, token, date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        String resultFileName = String.format(INVOICE_NAME_FORMAT, workingDatesCalculator.getDaysOfWorkingWeek(date));
        File resultFile = createTempFile(resultFileName);

        try  {
            documentTemplateEditor.create(templateVariablesFactory.create(date, payment), resultFile);
            invoiceSender.send(resultFile, resultFileName);
        } finally {
            if (!resultFile.delete()) {
                logger.warn("Cannot delete tmp file with invoice");
            }
        }
    }

    private File createTempFile(String resultFileName) throws InvoiceException {
        try {
            return File.createTempFile(resultFileName, "");
        } catch (IOException e) {
            throw new InvoiceException("Cannot create a tmp file to store invoice", e);
        }
    }
}