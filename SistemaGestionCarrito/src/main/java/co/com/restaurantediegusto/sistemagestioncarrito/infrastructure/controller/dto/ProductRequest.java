package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        Long id,
        @NotBlank(message = "El nombre es obligatorio") String name,
        @NotBlank(message = "La categoría es obligatoria") String category,
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a cero") BigDecimal price
) {
}
