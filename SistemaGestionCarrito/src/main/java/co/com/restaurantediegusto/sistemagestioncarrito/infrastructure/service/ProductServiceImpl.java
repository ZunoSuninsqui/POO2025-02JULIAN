package co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.service;

import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.Product;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.service.ProductService;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.entity.ProductEntity;
import co.com.restaurantediegusto.sistemagestioncarrito.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductJpaRepository productRepository;

    public ProductServiceImpl(ProductJpaRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = productRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Product update(Product product) {
        ProductEntity entity = productRepository.findById(product.id())
                .orElseThrow(() -> new IllegalStateException("No se encontró el producto a actualizar"));
        entity.setName(product.name());
        entity.setCategory(product.category());
        entity.setPrice(product.price());
        return toDomain(productRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceBetween(min, max).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByPriceGreaterThan(BigDecimal price) {
        return productRepository.findByPriceGreaterThan(price).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getCategory(), entity.getPrice());
    }

    private ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.id());
        entity.setName(product.name());
        entity.setCategory(product.category());
        entity.setPrice(product.price());
        return entity;
    }
}
