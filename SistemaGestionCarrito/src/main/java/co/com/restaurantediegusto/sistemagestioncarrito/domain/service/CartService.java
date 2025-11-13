package co.com.restaurantediegusto.sistemagestioncarrito.domain.service;

import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.CartItem;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Gestiona la persistencia del carrito de compras.
 */
public interface CartService {

    List<CartItem> findAll();

    Optional<CartItem> findByProductId(Long productId);

    CartItem save(Product product, int quantity);

    CartItem updateQuantity(Product product, int quantity);

    void deleteByProductId(Long productId);

    void clear();

    boolean isEmpty();
}
