package vn.attendance.handle;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.attendance.exception.BadRequestException;
import vn.attendance.exception.ExternalException;
import vn.attendance.exception.NotFoundException;
import vn.attendance.helper.APIResponseError;
import vn.attendance.util.DataUtil;

import java.io.IOException;

/**
 * ELearningExceptionHandler ExceptionHandler
 */
@ControllerAdvice
public class ELearningExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(ELearningExceptionHandler.class);

    /**
     * Handle {@link AuthenticationException}
     *
     * @param e {@link AuthenticationException}
     */
    @ExceptionHandler(value = {vn.attendance.exception.AuthenticationException.class})
    protected ResponseEntity<APIResponseError> handleAuthenticationException(vn.attendance.exception.AuthenticationException e) {
        this.logErrorException(e, HttpStatus.UNAUTHORIZED.toString());
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(e.getError())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<APIResponseError>(apiResponseError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<APIResponseError> handleAuthenticationException1(AuthenticationException e) {
        this.logErrorException(e, HttpStatus.UNAUTHORIZED.toString());
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(e.getMessage())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<APIResponseError>(apiResponseError, HttpStatus.UNAUTHORIZED);
    }

    /**
     * @returm {@link ResponseEntity}
     */
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<APIResponseError> handleNotFoundException(NotFoundException e) {
        this.logErrorException(e, HttpStatus.NOT_FOUND.toString());
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .error(e.getError())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : "Unknown error")
                .build();
        return new ResponseEntity<>(apiResponseError, HttpStatus.NOT_FOUND);
    }

    /**
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @ExceptionHandler(value = BadRequestException.class)
    protected ResponseEntity<APIResponseError> handleBadRequestException(BadRequestException e) {
        this.logErrorException(e, HttpStatus.BAD_REQUEST.toString());
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(e.getError())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : "Unknown error")
                .build();
        return new ResponseEntity<APIResponseError>(apiResponseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponseError> handleAccessDeniedException(AccessDeniedException e) {
        this.logErrorException(e, HttpStatus.UNAUTHORIZED.toString());
        APIResponseError apiResponseError = APIResponseError.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.name())
                .message(!StringUtils.isEmpty(e.getMessage()) ? e.getMessage() : "Access Denied")
                .build();
        return new ResponseEntity<APIResponseError>(apiResponseError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExternalException.class)
    public ResponseEntity<APIResponseError> handleExternalException(ExternalException e) {
//        log.error(e.getMessage(), e);
        APIResponseError apiResponseError = new APIResponseError(e.getError_code(), e.getMessage());
        return new ResponseEntity<APIResponseError>(apiResponseError, HttpStatus.BAD_REQUEST);
    }

    /**
     * fog error exception
     *
     * @param ex
     * @param httpStatus
     */
    private void logErrorException(Exception ex, String httpStatus) {
        try {
            JsonObject json = new JsonObject();
            String stacktrace = ExceptionUtils.getStackTrace(ex);
            stacktrace = DataUtil.formatSpaceString(stacktrace);
            json.addProperty("HttpStatus", httpStatus);
            json.addProperty("stacktrace", stacktrace);
            log.error(DataUtil.formatSpaceString(json.toString()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
