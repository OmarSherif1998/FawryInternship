package model;

public class Product {

    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = newName;
    }

    public void setPrice(int itemPrice) {
        if (itemPrice < 0) {
            throw new IllegalArgumentException("Price cannot be less than 0");
        }
        this.price = itemPrice;
    }

    public void increaseQuantity(int quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity increase cannot be less than 0");
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) {
        if (quantity < 0 || this.quantity < quantity) {
            throw new IllegalArgumentException("Quantity increase cannot be a positive number");
        }
        this.quantity -= quantity;
    }

}
