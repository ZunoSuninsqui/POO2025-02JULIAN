package co.com.restaurantediegusto.sistemagestioncarrito.domain.exception;

/**
 * Excepción para reglas de negocio incumplidas.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
