package services;

import java.util.List;
import interfaces.Shippable;
import interfaces.ShippingService;

public class SimpleShippingService implements ShippingService {
    @Override
    public void shipItems(List<Shippable> items) {
        double totalWeight = items.stream()
                .mapToDouble(Shippable::getWeight)
                .sum();

        System.out.println("\n** Shipping Summary **");
        System.out.printf("Shipping %d items\n", items.size());
        System.out.printf("Total weight: %.2f kg\n", totalWeight);
        items.forEach(item -> System.out.printf("- %s (%.2f kg)\n", item.getName(), item.getWeight()));
    }
}
