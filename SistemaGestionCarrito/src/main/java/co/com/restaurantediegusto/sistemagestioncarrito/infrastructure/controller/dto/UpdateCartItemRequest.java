package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequest(
        @Min(value = 0, message = "La cantidad no puede ser negativa") int quantity
) {
}
