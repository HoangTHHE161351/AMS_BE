package vn.attendance.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Authentication Exception
 */
@Getter
@Setter
public class ExternalException extends Exception {
    private static final long serialVersionUID = 1L;
    private String error_code;
    private String message;

    public ExternalException() {
    }

    public ExternalException(String error_code, String message) {
        super(message);
        this.error_code = error_code;
        this.message = message;
    }
}
