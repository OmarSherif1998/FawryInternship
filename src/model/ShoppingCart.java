package model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Product, Integer> items = new HashMap<>();

    public void addItem(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock for " + product.getName());
        }

        items.merge(product, quantity, Integer::sum);
    }

    public double calculateSubTotal() {
        return items.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();

        // entrySet() Converts a Map into pairs of keys+values
        // stream() Enables step-by-step processing (like a factory assembly line)
        // mapToDouble() Transforms each item (here: calculates price × quantity)
        // sum() Adds all numbers in the stream
    }

    public Map<Product, Integer> getItems() {

        return new HashMap<>(items);
    }
}
