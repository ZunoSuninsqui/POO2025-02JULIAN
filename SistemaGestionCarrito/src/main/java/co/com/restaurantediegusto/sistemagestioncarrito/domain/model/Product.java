package co.com.restaurantediegusto.sistemagestioncarrito.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa un producto disponible en el catálogo de la tienda.
 */
public record Product(Long id, String name, String category, BigDecimal price) {

    public Product {
        Objects.requireNonNull(name, "El nombre del producto es obligatorio");
        Objects.requireNonNull(category, "La categoría del producto es obligatoria");
        Objects.requireNonNull(price, "El precio del producto es obligatorio");
    }
}
