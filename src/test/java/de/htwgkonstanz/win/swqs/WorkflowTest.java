package de.htwgkonstanz.win.swqs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowTest {

    @Mock
    CoinDeposit c;
    @Mock
    Dispenser d;
    @Mock
    PriceTable p;

    Workflow w;

    @Before
    public void setup() {
       w = new Workflow(d, c, p);
        when(d.checkItem(eq(1))).thenReturn(false);
        when(d.checkItem(eq(2))).thenReturn(true);
        when(p.getPrice(anyInt())).thenReturn(new BigDecimal("1"));
    }

    @Test
    public void selectCase1() {
        //setup
        w.coinsIn(new BigDecimal("2"));

        // execute
        int code = w.select(2);

        // verify
        verify(c).dispenseCoins(new BigDecimal(1));
        verify(d).dispenseItem(2);
        assertEquals(0,code);
    }

    @Test
    public void selectCase2() {
        // Setup
        w.coinsIn(new BigDecimal("1"));

        // execute
        int code = w.select(1);

        // verify
        assertEquals(-1,code);
    }

    @Test
    public void selectCase3() {
        // Setup
        w.coinsIn(new BigDecimal("0.5"));

        // execute
        int code = w.select(2);

        // verify
        assertEquals(-2,code);
    }

    @Test
    public void selectCase4() {
        // Setup
        w.coinsIn(new BigDecimal("1"));

        // execute
        int code = w.select(2);

        // verify
        verify(c,never()).dispenseCoins(any(BigDecimal.class));
        verify(d).dispenseItem(2);
        assertEquals(0,code);
    }
}