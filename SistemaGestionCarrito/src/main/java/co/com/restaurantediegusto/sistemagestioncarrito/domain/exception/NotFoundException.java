package co.com.restaurantediegusto.sistemagestioncarrito.domain.exception;

/**
 * Excepción lanzada cuando un recurso no existe.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
