package vn.attendance.exception;

import lombok.Getter;
import lombok.Setter;
import vn.attendance.util.DataUtils;
import vn.attendance.util.MessageCode;

/**
 * Authentication Exception
 */
@Getter
@Setter
public class AmsException extends Exception {
    private static final long serialVersionUID = -4998452844437648420L;

    private final String code;

    private final String message;

    public AmsException(MessageCode messageCode) {
        code = messageCode.getCode();
        message = messageCode.getCode();
    }

    public AmsException(String messageCode) {
        code = "500";
        message = messageCode;
    }
    public AmsException(MessageCode messageCode, String field) {
        code = messageCode.getCode();
        message = DataUtils.isNullOrEmpty(field) ? messageCode.getCode() : field;
    }

    public AmsException(MessageCode messageCode, String rawMessage, boolean isRawMessage) {
        code = messageCode.getCode();
        message = rawMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}