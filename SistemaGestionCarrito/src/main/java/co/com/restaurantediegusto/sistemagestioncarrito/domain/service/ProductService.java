package co.com.restaurantediegusto.sistemagestioncarrito.domain.service;

import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Contrato para interactuar con la persistencia de productos.
 */
public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    Product update(Product product);

    void deleteById(Long id);

    List<Product> findByCategory(String category);

    List<Product> findByPriceRange(BigDecimal min, BigDecimal max);

    List<Product> findByPriceGreaterThan(BigDecimal price);

    boolean existsById(Long id);
}
