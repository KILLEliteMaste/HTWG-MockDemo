package de.htwgkonstanz.win.swqs.vendingmachine;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WorkflowBackdoorTest {
    private PriceTableImpl priceTable;

    @BeforeEach
    public void setupPriceTable() {
        priceTable = new PriceTableImpl();
        priceTable.setPrice(1, new BigDecimal("1"));
        priceTable.setPrice(2, new BigDecimal("1.10"));
        priceTable.setPrice(3, new BigDecimal("1"));
        priceTable.setPrice(4, new BigDecimal("1"));
        priceTable.setPrice(5, new BigDecimal("2.50"));
        priceTable.setPrice(6, new BigDecimal("3.50"));
        priceTable.setPrice(7, new BigDecimal("4"));
        priceTable.setPrice(8, new BigDecimal("2.80"));
        priceTable.setPrice(9, new BigDecimal("2.50"));
        priceTable.setPrice(10, new BigDecimal("2"));
        priceTable.setPrice(11, new BigDecimal("1.30"));
        priceTable.setPrice(12, new BigDecimal("2.30"));
        priceTable.setPrice(13, new BigDecimal("2.50"));
        priceTable.setPrice(14, new BigDecimal("1.10"));
        priceTable.setPrice(15, new BigDecimal("3"));
        priceTable.setPrice(16, new BigDecimal("1.10"));
        priceTable.setPrice(17, new BigDecimal("2.50"));
        priceTable.setPrice(18, new BigDecimal("0.90"));
        priceTable.setPrice(19, new BigDecimal("3.50"));
        priceTable.setPrice(20, new BigDecimal("1.30"));
        priceTable.setPrice(21, new BigDecimal("2.50"));
        priceTable.setPrice(22, new BigDecimal("1"));
        priceTable.setPrice(23, new BigDecimal("1.80"));
        priceTable.setPrice(24, new BigDecimal("2.30"));
    }

    private Dispenser createDispenser() {
        return new Dispenser() {
            public boolean checkItem(int item) {
                return (item < 4) || ((item % 4) == 1);
            }

            public void dispenseItem(int item) {

            }
        };
    }

    /**
     * Test with backdoor setup and dummys.
     */
    @Test
    public void testNotEnoughMoney() {
        // dispenser and coin deposit have dummy values
        Dispenser dispenser = createDispenser();
        Workflow workflow = new Workflow(dispenser, null, priceTable, new Display() {
            String msg = "";

            @Override
            public void setMessage(String message) {
                msg = message;
            }
        });

        // execute
        workflow.coinsIn(new BigDecimal("1"));
        int resultCode = workflow.select(1);

        //verify
        assertEquals(0, resultCode);
    }

    /**
     * Test with backdoor setup, stub and dummy.
     */
    @Test
    public void testNotInMachine() {
        Dispenser dispenser = createDispenser();

        // dispenser and coin deposit have dummy values
        Workflow workflow = new Workflow(dispenser, null, priceTable, new Display() {
            String msg = "";

            @Override
            public void setMessage(String message) {
                msg = message;
            }
        });

        // execute
        workflow.coinsIn(new BigDecimal("2"));
        workflow.coinsIn(new BigDecimal("2"));
        int resultCode = workflow.select(6);

        //verify
        assertEquals(-2, resultCode);
    }
}
