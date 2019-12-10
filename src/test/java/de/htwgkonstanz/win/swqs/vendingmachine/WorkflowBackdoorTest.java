package de.htwgkonstanz.win.swqs.vendingmachine;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class WorkflowBackdoorTest {

    /**
     * Test with backdoor setup and dummys.
     */
    @Test
    public void testNotEnoughMoney() {
        // Setup
        // Use Backdoor setup for price table, item 1 has price 2.00
        PriceTableImpl priceTable = new PriceTableImpl();
        priceTable.setPrice(1,new BigDecimal("2"));
        // dispenser and coin deposit have dummy values
        Workflow workflow = new Workflow(null, null, priceTable);

        // execute
        workflow.coinsIn(new BigDecimal("1"));
        int resultCode = workflow.select(1);

        //verify
        assertEquals(-1, resultCode);
    }

    /**
     * Test with backdoor setup, stub and dummy.
     */
    @Test
    public void testNotInMachine() {
        // Setup
        // Use Backdoor setup for price table, item 1 has price 2.00
        PriceTableImpl priceTable = new PriceTableImpl();
        priceTable.setPrice(1,new BigDecimal("2"));
        // Simple stub which returns always false
        Dispenser dispenser = new Dispenser() {
            public boolean checkItem(int item) {
                return false;
            }

            public void dispenseItem(int item) {

            }
        };

        // dispenser and coin deposit have dummy values
        Workflow workflow = new Workflow(dispenser, null, priceTable);

        // execute
        workflow.coinsIn(new BigDecimal("2"));
        int resultCode = workflow.select(1);

        //verify
        assertEquals(-2, resultCode);
    }
}
