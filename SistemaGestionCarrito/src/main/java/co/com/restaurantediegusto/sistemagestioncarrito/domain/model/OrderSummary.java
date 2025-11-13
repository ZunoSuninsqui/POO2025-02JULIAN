package co.com.restaurantediegusto.sistemagestioncarrito.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resumen generado al cerrar un pedido.
 */
public record OrderSummary(Long orderId,
                           LocalDateTime createdAt,
                           List<OrderItem> items,
                           BigDecimal totalBeforeDiscount,
                           BigDecimal discount,
                           BigDecimal totalToPay) {
}
