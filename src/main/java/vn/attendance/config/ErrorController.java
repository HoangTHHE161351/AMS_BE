package vn.attendance.config;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.attendance.exception.ExternalException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExternalException> handleException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        throw exception;
    }
}