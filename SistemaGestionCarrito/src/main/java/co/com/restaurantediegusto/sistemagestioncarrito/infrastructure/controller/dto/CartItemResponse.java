package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto;

import java.math.BigDecimal;

public record CartItemResponse(Long productId,
                               String name,
                               String category,
                               BigDecimal unitPrice,
                               int quantity,
                               BigDecimal subtotal) {
}
