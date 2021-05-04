package de.htwgkonstanz.win.swqs.vendingmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WorkflowTestMockAnnotations {

    @Mock
    CoinDeposit c;
    @Mock
    Dispenser d;
    @Mock
    PriceTable p;

    Workflow w;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(d.checkItem(eq(1))).thenReturn(false);
        when(d.checkItem(eq(2))).thenReturn(true);
        when(p.getPrice(anyInt())).thenReturn(new BigDecimal("1"));
        w = new Workflow(d, c, p);
    }

    @Test
    public void selectWithChange() {
        //setup
        w.coinsIn(new BigDecimal("2"));

        // execute
        int code = w.select(2);

        // verify
        verify(c).dispenseCoins(new BigDecimal(1));
        verify(d).dispenseItem(2);
        assertEquals(0, code);
    }

    @Test
    public void selectEmpty() {
        // Setup
        w.coinsIn(new BigDecimal("1"));

        // execute
        int code = w.select(1);

        // verify
        assertEquals(-2, code);
    }

    @Test
    public void selectNotEnoughMoney() {
        // Setup
        w.coinsIn(new BigDecimal("0.5"));

        // execute
        int code = w.select(2);

        // verify
        assertEquals(-1, code);
    }

    @Test
    public void selectCaseNoChange() {
        // Setup
        w.coinsIn(new BigDecimal("1"));

        // execute
        int code = w.select(2);

        // verify
        verify(c, never()).dispenseCoins(any(BigDecimal.class));
        verify(d).dispenseItem(2);
        assertEquals(0, code);
    }

    @Test
    public void cancel() {
        // Setup
        w.coinsIn(new BigDecimal("1"));

        // execute
        w.cancel();

        // verify
        verify(c, times(1)).dispenseCoins(new BigDecimal("1"));
        verify(d, never()).dispenseItem(anyInt());
    }
}