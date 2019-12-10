package de.htwgkonstanz.win.swqs.vendingmachine;

import java.math.BigDecimal;

public interface PriceTable {
    public BigDecimal getPrice(int item);
}
