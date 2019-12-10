package de.htwgkonstanz.win.swqs.vendingmachine;

import java.math.BigDecimal;

public class Workflow {

    private BigDecimal balance = BigDecimal.ZERO;
    private WorkflowState workflowState = WorkflowState.IDLE;

    private Dispenser dispenser;
    private CoinDeposit coinDeposit;
    private PriceTable priceTable;

    public Workflow(Dispenser dispenser, CoinDeposit coinDeposit, PriceTable priceTable) {
        this.dispenser = dispenser;
        this.coinDeposit = coinDeposit;
        this.priceTable = priceTable;
    }

    /**
     * Coins are inserted.
     */
    public void coinsIn(BigDecimal amount) {
        if (workflowState == WorkflowState.IDLE) {
            setBalance(amount);
            workflowState = WorkflowState.COLLECTING_MONEY;
        } else {
            addToBalance(amount);
        }
    }

    /**
     * Cancel transaction and refund coins.
     */
    public void cancel() {
        refundCoins (balance);
        workflowState = WorkflowState.IDLE;
    }

    /**
     * Executed when event select is fired.
     *
     * @param item the id of the selected item.
     * @return code  0 if the selection was successful,
     *              -1 if price is higher than inserted coins,
     *              -2 if selection is empty.
     */
    public int select(int item) {
        BigDecimal preis = priceTable.getPrice(item);
        if (balance.compareTo(preis) < 0) {
            return -1;
        }
        boolean isEmpty = !dispenser.checkItem(item);
        if (isEmpty) {
            return -2;
        }
        BigDecimal change = balance.subtract(preis);
        if (change.intValue() > 0) {
            coinDeposit.dispenseCoins(change);
        }
        dispenser.dispenseItem(item);
        workflowState = WorkflowState.IDLE;
        return 0;
    }

    protected void refundCoins(BigDecimal amount) {
        coinDeposit.dispenseCoins(amount);
        balance = BigDecimal.ZERO;
    }

    protected void setBalance(BigDecimal amount) {
        this.balance = amount;
    }

    protected void addToBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    protected BigDecimal getBalance() {
        return balance;
    }
}
