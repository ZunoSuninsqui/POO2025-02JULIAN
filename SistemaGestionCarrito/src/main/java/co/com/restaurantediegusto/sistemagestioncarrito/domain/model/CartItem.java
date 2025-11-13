package co.com.restaurantediegusto.sistemagestioncarrito.domain.model;

import java.math.BigDecimal;

/**
 * Representa un ítem dentro del carrito de compras.
 */
public record CartItem(Long id, Product product, int quantity) {

    public BigDecimal subtotal() {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }
}
