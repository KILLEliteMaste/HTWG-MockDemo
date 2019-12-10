package de.htwgkonstanz.win.swqs.vendingmachine;

import java.math.BigDecimal;

public interface CoinDeposit {
    public void dispenseCoins(BigDecimal change);
}
