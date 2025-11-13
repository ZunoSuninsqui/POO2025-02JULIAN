package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.service;

import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.CartItem;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.Product;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.service.CartService;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.entity.CartItemEntity;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.entity.ProductEntity;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.repository.CartItemJpaRepository;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.repository.ProductJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemJpaRepository cartItemRepository;
    private final ProductJpaRepository productRepository;

    public CartServiceImpl(CartItemJpaRepository cartItemRepository, ProductJpaRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> findAll() {
        return cartItemRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CartItem> findByProductId(Long productId) {
        return cartItemRepository.findByProductId(productId)
                .map(this::toDomain);
    }

    @Override
    public CartItem save(Product product, int quantity) {
        ProductEntity productEntity = getProductReference(product.id());
        CartItemEntity entity = new CartItemEntity();
        entity.setProduct(productEntity);
        entity.setQuantity(quantity);
        return toDomain(cartItemRepository.save(entity));
    }

    @Override
    public CartItem updateQuantity(Product product, int quantity) {
        CartItemEntity entity = cartItemRepository.findByProductId(product.id())
                .orElseThrow(() -> new EntityNotFoundException("El producto no existe en el carrito"));
        entity.setQuantity(quantity);
        return toDomain(cartItemRepository.save(entity));
    }

    @Override
    public void deleteByProductId(Long productId) {
        cartItemRepository.findByProductId(productId)
                .ifPresent(cartItemRepository::delete);
    }

    @Override
    public void clear() {
        cartItemRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmpty() {
        return cartItemRepository.count() == 0;
    }

    private ProductEntity getProductReference(Long productId) {
        try {
            return productRepository.getReferenceById(productId);
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("No existe el producto con id " + productId);
        }
    }

    private CartItem toDomain(CartItemEntity entity) {
        ProductEntity product = entity.getProduct();
        Product domainProduct = new Product(product.getId(), product.getName(), product.getCategory(), product.getPrice());
        return new CartItem(entity.getId(), domainProduct, entity.getQuantity());
    }
}
