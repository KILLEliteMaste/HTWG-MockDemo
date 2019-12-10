package de.htwgkonstanz.win.swqs.vendingmachine;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Default Implementation of the price table used by the vending machine workflow.
 */
public class PriceTableImpl implements PriceTable {

    private HashMap<Integer,BigDecimal> prices = new HashMap<Integer,BigDecimal>();

    /**
     * Returns the price of the item.
     *
     * @param item the id of the item.
     * @return the price of the item.
     */
    public BigDecimal getPrice(int item) {
        return prices.get(item);
    }

    /**
     * Method for backdoor setup.
     *
     * @param item the id of the item.
     * @param price the price of the item.
     */
    public void setPrice(int item, BigDecimal price) {
        prices.put(item,price);
    }

    /**
     * In production proces would be loaded from csv file deployed with application.
     * Method is not yet implemented in the project.
     */
    public void loadFromFile() {
        // TODO
    }
}
