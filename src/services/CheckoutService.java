package services;

import interfaces.Shippable;
import interfaces.ShippingService;
import model.Customer;
import model.PerishableProduct;
import model.Product;
import model.ShoppingCart;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutService {
    private static final double SHIPPING_RATE_PER_KG = 10.0;
    private final ShippingService shippingService;

    public CheckoutService(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    public void checkout(Customer customer) {
        ShoppingCart cart = customer.getCart();

        // Validate cart not empty
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty, please add items before checking out.");
        }

        // Validate stock and expiration dates
        cart.getItems().forEach((product, quantity) -> {
            checkStock(product, quantity);
            checkExpiration(product);
        });

        // Calculate costs
        double subtotal = cart.calculateSubTotal();
        double shippingFee = calculateShippingFee(cart);
        double total = subtotal + shippingFee;

        // Validate customer balance
        if (customer.getBalance() < total) {
            throw new IllegalStateException("Insufficient funds");
        }

        // Process payment
        customer.deductBalance(total);

        // Update inventory
        cart.getItems().forEach((product, quantity) -> product.decreaseQuantity(quantity));

        // Process shipping
        List<Shippable> shippableItems = cart.getItems().entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Shippable)
                .flatMap(entry -> Collections.nCopies(
                        entry.getValue(),
                        (Shippable) entry.getKey()).stream())
                .collect(Collectors.toList());

        if (!shippableItems.isEmpty()) {
            shippingService.shipItems(shippableItems);
        }

        // Print receipt
        printReceipt(cart, subtotal, shippingFee, total, customer.getBalance());
    }

    private double calculateShippingFee(ShoppingCart cart) {
        return cart.getItems().entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Shippable)
                .mapToDouble(entry -> {
                    Shippable shippable = (Shippable) entry.getKey();
                    return shippable.getWeight() * entry.getValue() * SHIPPING_RATE_PER_KG;
                })
                .sum();
    }

    private void checkStock(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new IllegalStateException(
                    product.getName() + " is out of stock");
        }
    }

    private void checkExpiration(Product product) {
        if (product instanceof PerishableProduct) {
            PerishableProduct perishable = (PerishableProduct) product;
            if (perishable.isExpired()) {
                throw new IllegalStateException(
                        product.getName() + " has expired");
            }
        }
    }

    private void printReceipt(ShoppingCart cart, double subtotal,
            double shippingFee, double total, double balance) {
        System.out.println("\n** Shipment Notice **");
        cart.getItems().entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Shippable)
                .forEach(entry -> {
                    Shippable s = (Shippable) entry.getKey();
                    System.out.printf("%dx %s (%.2fkg)\n",
                            entry.getValue(), s.getName(), s.getWeight());
                });

        System.out.println("\n** Checkout Receipt **");
        cart.getItems().forEach((product, quantity) -> System.out.printf("%dx %s @ $%.2f: $%.2f\n",
                quantity, product.getName(),
                product.getPrice(), quantity * product.getPrice()));

        System.out.println("----------------------");
        System.out.printf("Subtotal: $%.2f\n", subtotal);
        System.out.printf("Shipping: $%.2f\n", shippingFee);
        System.out.printf("Total: $%.2f\n", total);
        System.out.printf("Remaining Balance: $%.2f\n", balance);
    }
}