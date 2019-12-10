package de.htwgkonstanz.win.swqs.vendingmachine;

public interface Dispenser {
    public boolean checkItem(int item);
    public void dispenseItem(int item);
}
