package de.htwgkonstanz.win.swqs.mockdemo;

import java.math.BigDecimal;

public class Workflow {

    private BigDecimal balance = BigDecimal.ZERO;

    private Dispenser dispenser;
    private CoinDeposit coinDeposit;
    private PriceTable priceTable;

    public Workflow(Dispenser dispenser, CoinDeposit coinDeposit, PriceTable priceTable) {
        this.dispenser = dispenser;
        this.coinDeposit = coinDeposit;
        this.priceTable = priceTable;
    }

    public void coinsIn(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void refundCoins(BigDecimal amount) {
        coinDeposit.dispenseCoins(amount);
        balance = BigDecimal.ZERO;
    }


    public int select(int item) {
        BigDecimal preis = priceTable.getPrice(item);
        boolean isEmpty = !dispenser.checkItem(item);
        if (isEmpty) {
            return -1;
        }
        if (balance.compareTo(preis) < 0) {
            return -2;
        }
        BigDecimal change = balance.subtract(preis);
        if (change.intValue() > 0) {
            coinDeposit.dispenseCoins(change);
        }
        dispenser.dispenseItem(item);
        return 0;
    }
}
