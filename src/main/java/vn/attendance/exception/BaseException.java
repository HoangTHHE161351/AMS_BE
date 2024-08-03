package vn.attendance.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message;

    public BaseException(String message) {
        super();
        this.message = message;
    }

}
