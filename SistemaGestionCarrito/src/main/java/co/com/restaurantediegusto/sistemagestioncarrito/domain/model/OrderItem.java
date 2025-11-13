package co.com.restaurantediegusto.sistemagestioncarrito.domain.model;

import java.math.BigDecimal;

/**
 * Elemento que compone el resumen de un pedido cerrado.
 */
public record OrderItem(Long productId,
                        String productName,
                        String category,
                        BigDecimal unitPrice,
                        int quantity,
                        BigDecimal subtotal) {
}
