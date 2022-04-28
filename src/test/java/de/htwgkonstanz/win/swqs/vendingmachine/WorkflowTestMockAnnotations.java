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
    @Mock
    Display display;

    Workflow w;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(d.checkItem(eq(1))).thenReturn(false);
        when(d.checkItem(eq(2))).thenReturn(true);
        when(p.getPrice(anyInt())).thenReturn(new BigDecimal("1"));
        w = new Workflow(d, c, p, display);
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
        verify(display, times(1)).setMessage("2");
        verify(display, times(1)).setMessage("0");
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
        verify(display, times(1)).setMessage("1");
        verify(display, times(1)).setMessage("Fach leer: 1");
    }

    @Test
    public void selectNotEnoughMoney() {
        // Setup
        w.coinsIn(new BigDecimal("0.5"));

        // execute
        int code = w.select(2);

        // verify
        assertEquals(-1, code);
        verify(display, times(1)).setMessage("0.5");
        verify(display, times(1)).setMessage("Nicht genug Guthaben. Item Preis: 1. Balance: 0.5");
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
        verify(display, times(1)).setMessage("1");
        verify(display, times(1)).setMessage("0");
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
        verify(display, times(1)).setMessage("1");
        verify(display, times(1)).setMessage("0");
    }
}