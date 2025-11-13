package co.com.restaurantediegusto.sistemagestioncarrito.application.usecase;

import co.com.restaurantediegusto.sistemagestioncarrito.domain.exception.BusinessException;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.exception.NotFoundException;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.CartItem;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.CartSummary;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.OrderSummary;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.Product;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.service.CartService;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.service.OrderService;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.service.ProductService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Casos de uso asociados al flujo del carrito de compras.
 */
@Component
public class CartUseCase {

    private static final BigDecimal DISCOUNT_THRESHOLD = BigDecimal.valueOf(100_000);
    private static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.05);

    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;

    public CartUseCase(CartService cartService, ProductService productService, OrderService orderService) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
    }

    public CartSummary viewCart() {
        List<CartItem> items = cartService.findAll();
        BigDecimal total = items.stream()
                .map(CartItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartSummary(items, total);
    }

    public CartItem addProduct(Long productId, int quantity) {
        validateQuantityGreaterThanZero(quantity);
        Product product = loadProduct(productId);
        return cartService.findByProductId(productId)
                .map(item -> cartService.updateQuantity(product, item.quantity() + quantity))
                .orElseGet(() -> cartService.save(product, quantity));
    }

    public CartItem updateProductQuantity(Long productId, int quantity) {
        if (quantity < 0) {
            throw new BusinessException("La cantidad no puede ser negativa");
        }
        Product product = loadProduct(productId);
        if (quantity == 0) {
            ensureItemExists(productId);
            cartService.deleteByProductId(productId);
            return null;
        }
        ensureItemExists(productId);
        return cartService.updateQuantity(product, quantity);
    }

    public void removeProduct(Long productId) {
        ensureItemExists(productId);
        cartService.deleteByProductId(productId);
    }

    public OrderSummary checkout() {
        List<CartItem> items = cartService.findAll();
        if (items.isEmpty()) {
            throw new BusinessException("No es posible cerrar el pedido con el carrito vacío");
        }
        BigDecimal subtotal = items.stream()
                .map(CartItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discount = calculateDiscount(subtotal);
        BigDecimal total = subtotal.subtract(discount);
        OrderSummary summary = orderService.saveOrder(items, subtotal, discount, total);
        cartService.clear();
        return summary;
    }

    public void cancelOrder() {
        if (cartService.isEmpty()) {
            throw new BusinessException("No hay un pedido pendiente por cancelar o ya fue cerrado");
        }
        cartService.clear();
    }

    private Product loadProduct(Long productId) {
        return productService.findById(productId)
                .orElseThrow(() -> new NotFoundException("No existe un producto con el id " + productId));
    }

    private void ensureItemExists(Long productId) {
        cartService.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("El producto no existe en el carrito"));
    }

    private void validateQuantityGreaterThanZero(int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero");
        }
    }

    private BigDecimal calculateDiscount(BigDecimal subtotal) {
        if (subtotal.compareTo(DISCOUNT_THRESHOLD) > 0) {
            return subtotal.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
}
