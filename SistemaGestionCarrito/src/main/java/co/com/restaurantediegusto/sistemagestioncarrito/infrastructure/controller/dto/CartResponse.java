package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(List<CartItemResponse> items, BigDecimal total) {
}
