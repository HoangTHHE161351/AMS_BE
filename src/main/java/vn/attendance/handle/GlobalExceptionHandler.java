package vn.attendance.handle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import vn.attendance.exception.EntityValidationException;
import vn.attendance.exception.PartialUpdateException;
import vn.attendance.exception.QueryValidationException;
import vn.attendance.exception.ResourceNotFoundException;
import vn.attendance.handle.dto.ErrorDetails;
import vn.attendance.helper.APIResponseError;
import vn.attendance.util.MapperUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<?> entityValidationException(EntityValidationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), ex.getDetails(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QueryValidationException.class)
    public ResponseEntity<?> queryValidationException(QueryValidationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PartialUpdateException.class)
    public ResponseEntity<?> partialUpdateException(PartialUpdateException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<?> optimisticLockingException(OptimisticLockingFailureException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                messageSource.getMessage(
                        "error.optimistic-locking",
                        new Object[0],
                        LocaleContextHolder.getLocale()),
                "",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "", request.getDescription(false));
        Map<String, String> errors = new HashMap<>();
        Class clazz = ex.getBindingResult().getTarget().getClass();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = MapperUtil.convertToJsonName(clazz, ((FieldError) error).getField());
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        if (!CollectionUtils.isEmpty(errors)) {
            errorDetails.setMessage(messageSource.getMessage(
                    "error.validation",
                    new Object[0],
                    LocaleContextHolder.getLocale()));
            errorDetails.setDetails(errors);
        }

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetail = new ErrorDetails(new Date(), ex.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        log.info(ex.getMessage());
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .message(!StringUtils.isEmpty(ex.getMessage()) ? ex.getMessage() : "Unknown error")
                .build();
        return new ResponseEntity<>(apiResponseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
