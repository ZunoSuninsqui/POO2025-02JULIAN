package co.com.restaurantediegusto.sistemagestioncarrito.domain.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Representa la vista del carrito para el usuario.
 */
public record CartSummary(List<CartItem> items, BigDecimal total) {
}
