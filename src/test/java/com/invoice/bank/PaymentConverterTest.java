package com.invoice.bank;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PaymentConverterTest {
    private final String input;
    private final String expectedOutput;

    public PaymentConverterTest(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void paymentFormatConversion() {
        assertEquals(expectedOutput, new PaymentConverter().convert(input));
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"112175.4700", "112175.47"},
                {"112175.47123", "112175.47"},
                {"112175.4766", "112175.47"},
                {"0", "0"},
                {"010.10", "10.10"},
                {"-010.1", "-10.10"}
        });
    }
}