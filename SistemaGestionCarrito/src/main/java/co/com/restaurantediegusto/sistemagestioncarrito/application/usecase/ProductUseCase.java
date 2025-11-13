package co.com.restaurantediegusto.sistemagestioncarrito.application.usecase;

import co.com.restaurantediegusto.sistemagestioncarrito.domain.exception.BusinessException;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.exception.NotFoundException;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.model.Product;
import co.com.restaurantediegusto.sistemagestioncarrito.domain.service.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Casos de uso relacionados con la administración del catálogo de productos.
 */
@Component
public class ProductUseCase {

    private final ProductService productService;

    public ProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getCatalog() {
        return productService.findAll();
    }

    public Product getProduct(Long id) {
        return productService.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe un producto con el id " + id));
    }

    public Product createProduct(Product product) {
        if (product.id() != null && productService.existsById(product.id())) {
            throw new BusinessException("Ya existe un producto con el id especificado");
        }
        return productService.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product existing = getProduct(id);
        return productService.update(new Product(existing.id(), product.name(), product.category(), product.price()));
    }

    public void deleteProduct(Long id) {
        if (!productService.existsById(id)) {
            throw new NotFoundException("No existe un producto con el id " + id);
        }
        productService.deleteById(id);
    }

    public List<Product> findByCategory(String category) {
        if (!StringUtils.hasText(category)) {
            throw new BusinessException("La categoría es obligatoria");
        }
        return productService.findByCategory(category.trim());
    }

    public List<Product> findByPriceGreaterThan(BigDecimal price) {
        if (price == null) {
            throw new BusinessException("El precio de comparación es obligatorio");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El precio mínimo debe ser positivo");
        }
        return productService.findByPriceGreaterThan(price);
    }

    public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new BusinessException("Debe especificar el precio mínimo y máximo");
        }
        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Los valores de precio deben ser positivos");
        }
        if (min.compareTo(max) > 0) {
            throw new BusinessException("El precio mínimo no puede ser mayor que el máximo");
        }
        return productService.findByPriceRange(min, max);
    }
}
