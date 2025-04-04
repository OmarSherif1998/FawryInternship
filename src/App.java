import java.time.LocalDate;

import interfaces.ShippingService;
import model.Customer;
import model.NonPerishableProduct;
import model.PerishableProduct;
import services.CheckoutService;
import services.SimpleShippingService;

public class App {
        public static void main(String[] args) {
                // Create products
                PerishableProduct cheese = new PerishableProduct(
                                "Cheese", 5.99, 10, LocalDate.now().plusDays(7), 0.5);

                NonPerishableProduct tv = new NonPerishableProduct(
                                "Smart TV", 899.99, 5);

                PerishableProduct milk = new PerishableProduct(
                                "Milk", 3.49, 15, LocalDate.now().plusDays(3), 1.0);

                // Create customer
                Customer customer = new Customer("John Doe", 1000.00);

                // Add items to cart
                customer.getCart().addItem(cheese, 2);
                customer.getCart().addItem(tv, 1);
                customer.getCart().addItem(milk, 3);

                // Checkout
                ShippingService shippingService = new SimpleShippingService();
                CheckoutService checkout = new CheckoutService(shippingService);
                checkout.checkout(customer);
        }
}