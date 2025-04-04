package interfaces;

import java.util.List;

public interface ShippingService {
    void shipItems(List<Shippable> items);
}
