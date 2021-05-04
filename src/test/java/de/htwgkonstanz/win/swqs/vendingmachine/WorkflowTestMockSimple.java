package de.htwgkonstanz.win.swqs.vendingmachine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WorkflowTestMockSimple {

    @Test
    public void testNotInMachine() {
        // Setup
        Dispenser d = mock(Dispenser.class);
        when(d.checkItem(eq(1))).thenReturn(false);
        PriceTable p = mock(PriceTable.class);
        when(p.getPrice(anyInt())).thenReturn(new BigDecimal("1"));
        Workflow w = new Workflow(d, mock(CoinDeposit.class), p);

        // execute
        w.coinsIn(new BigDecimal("1"));
        int code = w.select(1);

        // verify
        assertEquals(-2, code);
    }
}