package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String category, BigDecimal price) {
}
