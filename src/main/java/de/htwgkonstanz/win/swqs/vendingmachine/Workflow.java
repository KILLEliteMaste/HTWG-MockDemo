package de.htwgkonstanz.win.swqs.vendingmachine;

import java.math.BigDecimal;

public class Workflow {

    private BigDecimal balance = BigDecimal.ZERO;
    private WorkflowState workflowState = WorkflowState.IDLE;

    private Dispenser dispenser;
    private CoinDeposit coinDeposit;
    private PriceTable priceTable;
    private Display display;

    public Workflow(Dispenser dispenser, CoinDeposit coinDeposit, PriceTable priceTable, Display display) {
        this.dispenser = dispenser;
        this.coinDeposit = coinDeposit;
        this.priceTable = priceTable;
        this.display = display;
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
        refundCoins(balance);
        workflowState = WorkflowState.IDLE;
    }

    /**
     * Executed when event select is fired.
     *
     * @param item the id of the selected item.
     * @return code  0 if the selection was successful,
     * -1 if price is higher than inserted coins,
     * -2 if selection is empty.
     */
    public int select(int item) {
        BigDecimal preis = priceTable.getPrice(item);
        if (balance.compareTo(preis) < 0) {
            display.setMessage("Nicht genug Guthaben. Item Preis: " + preis + ". Balance: " + balance);
            return -1;
        }
        boolean isEmpty = !dispenser.checkItem(item);
        if (isEmpty) {
            display.setMessage("Fach leer: " + item);
            return -2;
        }
        BigDecimal change = balance.subtract(preis);
        balance = BigDecimal.ZERO;
        display.setMessage(balance.toString());
        if (change.doubleValue() > 0) {
            coinDeposit.dispenseCoins(change);
        }
        dispenser.dispenseItem(item);
        workflowState = WorkflowState.IDLE;
        return 0;
    }

    protected void refundCoins(BigDecimal amount) {
        coinDeposit.dispenseCoins(amount);
        balance = BigDecimal.ZERO;
        display.setMessage(balance.toString());
    }

    protected void setBalance(BigDecimal amount) {
        this.balance = amount;
        display.setMessage(amount.toString());
    }

    protected void addToBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        display.setMessage(balance.toString());
    }

    protected BigDecimal getBalance() {
        return balance;
    }
}
