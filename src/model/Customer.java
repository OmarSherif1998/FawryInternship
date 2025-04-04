package model;

public class Customer {
    private String name;
    private double balance;
    private ShoppingCart cart = new ShoppingCart();

    public Customer(String name, double balance) {

        this.name = name;
        this.balance = balance;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public double getBalance() {

        return balance;
    }

    public String getName() {
        return name;
    }

    public void deductBalance(double deductableAmount) {

        if (balance < deductableAmount)
            throw new IllegalArgumentException("insfficient balance");
        balance -= deductableAmount;
    }
}
