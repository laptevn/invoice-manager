package com.invoice.template;

import com.invoice.InvoiceException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class DocumentTemplateEditor {
    private final Logger logger = LoggerFactory.getLogger(DocumentTemplateEditor.class);

    private final Resource templateResource;

    public DocumentTemplateEditor(@Value("classpath:template_invoice.docx") Resource templateResource) throws IOException {
        this.templateResource = templateResource;
    }

    public void create(Map<String, String> data, File resultFile) throws InvoiceException {
        logger.info("Creating new document.");

        try (XWPFDocument document = new XWPFDocument(OPCPackage.open(templateResource.getInputStream()))) {
            for (XWPFTable table : document.getTables()) {
                handleTable(data, table);
            }

            try (FileOutputStream outputStream = new FileOutputStream(resultFile)) {
                document.write(outputStream);
            }
        } catch (InvalidFormatException | IOException e) {
            throw new InvoiceException("Cannot create invoice file", e);
        }

        logger.info("Created new document.");
    }

    private void handleTable(Map<String, String> data, XWPFTable table) {
        for (XWPFTableRow row : table.getRows()) {
            handleRow(data, row);
        }
    }

    private void handleRow(Map<String, String> data, XWPFTableRow row) {
        for (XWPFTableCell cell : row.getTableCells()) {
            handleCell(data, cell);
        }
    }

    private void handleCell(Map<String, String> data, XWPFTableCell cell) {
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            handleParagraph(data, paragraph);
        }
    }

    private void handleParagraph(Map<String, String> data, XWPFParagraph paragraph) {
        for (XWPFRun run : paragraph.getRuns()) {
            for (int i = 0; i < run.getCTR().sizeOfTArray(); i++) {
                String text = run.getText(i);
                for (Map.Entry<String, String> subst : data.entrySet()) {
                    text = text.replaceAll(subst.getKey(), subst.getValue());
                }
                run.setText(text, i);
            }
        }
    }
}