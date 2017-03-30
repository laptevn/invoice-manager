package com.invoice.bank;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RunWith(Parameterized.class)
public class PaymentProviderTest {
    private final String input;
    private final String expectedOutput;

    public PaymentProviderTest(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void paymentFormatConversion() {
        Operation operation = new Operation();
        operation.setAmount(input);
        PaymentProvider paymentProvider = new PaymentProvider(createClient(operation));
        assertEquals(expectedOutput, paymentProvider.loadPayment(null, null, null));
    }

    private static Client createClient(Operation operation) {
        Client client = EasyMock.createMock(Client.class);
        EasyMock.expect(client.getLatestOperation(EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject()))
                .andReturn(Collections.singletonList(operation));
        EasyMock.replay(client);
        return client;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"112175.4700", "112175.47"},
                {"112175.47123", "112175.47"},
                {"112175.4766", "112175.47"},
                {"0", "0"},
                {"010.10", "10.1"},
                {"-010.10", "-10.1"}
        });
    }
}