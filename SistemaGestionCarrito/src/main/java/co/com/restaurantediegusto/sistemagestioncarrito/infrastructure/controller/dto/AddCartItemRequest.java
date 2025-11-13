package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(
        @NotNull(message = "El id del producto es obligatorio") Long productId,
        @Min(value = 1, message = "La cantidad debe ser mayor a cero") int quantity
) {
}
