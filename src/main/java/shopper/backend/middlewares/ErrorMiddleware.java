package shopper.backend.middlewares;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import shopper.backend.constants.ErrorConstants;
import shopper.backend.exceptions.ConflictException;
import shopper.backend.exceptions.NotFoundException;
import shopper.backend.responses.ErrorResponsePayload;

@ControllerAdvice
public class ErrorMiddleware {
    private final Logger logger = LoggerFactory.getLogger(ErrorMiddleware.class);

    // Tratează excepțiile de tip NotFoundException și returnează un răspuns HTTP NOT_FOUND
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponsePayload> handleNotFoundException(NotFoundException e) {
        this.logger.error("Not found error", e);
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponsePayload);
    }

    // Tratează excepțiile de tip ConflictException și returnează un răspuns HTTP CONFLICT
    @ExceptionHandler(ConflictException.class)
    public final ResponseEntity<ErrorResponsePayload> handleConflictException(ConflictException e) {
        this.logger.error("Conflict error", e);
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponsePayload);
    }

    // Tratează orice altă excepție și returnează un răspuns HTTP INTERNAL_SERVER_ERROR
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponsePayload> handleAllExceptions(Exception e) {
        this.logger.error("Internal server error", e);
        ErrorResponsePayload errorResponsePayload = new ErrorResponsePayload(ErrorConstants.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponsePayload);
    }
}
