package vn.attendance.helper;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import vn.attendance.util.APIConstants;
import vn.attendance.util.MessageCode;

public class ApiResponse<T> extends ResponseEntity {

    public ApiResponse(HttpStatus status) {
        this(APIBody.builder().code(APIConstants.SUCCESS_CODE).build(), null, status);
    }

    public ApiResponse(T body, HttpStatus status) {
        this(APIBody.builder().code(APIConstants.SUCCESS_CODE).data(body).build(), null, status);
    }

    public ApiResponse(T body, MultiValueMap<String, String> headers,HttpStatus status) {
        this(APIBody.builder().code(APIConstants.SUCCESS_CODE).data(body).build(), headers, status);
    }


    public ApiResponse(String errorCode, String errMess) {
        this(APIBody.builder().code(APIConstants.ERROR_GACHA_SERVER).data(null).errorCode(errorCode).message(errMess).build(), null, null);
    }

    public ApiResponse(int code, T body) {
        this(APIBody.builder().code(code).data(body).build(), null, HttpStatus.OK);
    }

    public ApiResponse(T body, long pageNo, long pageSize, long totalPages, HttpStatus status) {
        this(APIBody.builder().code(APIConstants.SUCCESS_CODE).data(body).pageNo(pageNo).pageSize(pageSize).totalPages(totalPages).build(), null, status);
    }

    public ApiResponse(int code, T body, HttpStatus httpStatus) {
        this(APIBody.builder().code(code).data(body).build(), null, httpStatus);
    }

    public ApiResponse(int code, String message, HttpStatus httpStatus) {
        this(APIBody.builder().code(code).message(message).build(), null, httpStatus);
    }

    public ApiResponse(int code, T body, MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        this(APIBody.builder().code(code).data(body).build(), headers, httpStatus);
    }

    public ApiResponse(int code, String errorCode, String mess, HttpStatus httpStatus) {
        this((T) APIBody.builder().code(code).errorCode(errorCode).data(mess).build(),  httpStatus);
    }

    public ApiResponse(int code, String errorCode, T body, MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        this(APIBody.builder().errorCode(errorCode).code(code).data(body).build(), headers, httpStatus);
    }

    public ApiResponse(int code, MessageCode messageCode, HttpStatus httpStatus) {
        this((T) APIBody.builder().code(code).data(messageCode.getCode()).message(messageCode.getCode()).build(), httpStatus);
    }

    public ApiResponse(APIBody body, MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        super(body, headers, httpStatus);
    }

    public static <T> ApiResponse<T> okStatus(T body) {
        return new ApiResponse<T>(body, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> okStatus(T body, long pageNo, long pageSize, long totalPages) {
        return new ApiResponse<T>(body, pageNo, pageSize, totalPages, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> okStatus() {
        return new ApiResponse<T>( HttpStatus.OK);
    }

    public static <T> ApiResponse<T> okStatus(T body, int code) {
        return new ApiResponse<T>(code, body, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> okStatus(T body, MultiValueMap<String, String> headers) {
        return new ApiResponse<T>(body, headers,HttpStatus.OK);
    }

    public static <T> ApiResponse<T> createdStatus(T body) {
        return new ApiResponse<T>(APIConstants.SUCCESS_CODE, body, HttpStatus.CREATED);
    }

    public static <T> ApiResponse<T> errorStatus(T body, HttpStatus httpStatus) {
        return new ApiResponse<T>(body, httpStatus);
    }

    public static <T> ApiResponse<T> errorStatus(int code, T body, HttpStatus httpStatus) {
        return new ApiResponse<T>(code, body, httpStatus);
    }

    public static <T> ApiResponse<T> errorStatus(String message) {
        return new ApiResponse<T>(APIConstants.ERROR_CODE, message, HttpStatus.BAD_REQUEST);
    }



    public static <T> ApiResponse<T> errorStatus(String message, String errorCode) {
        return new ApiResponse<T>(errorCode, message);
    }

    public static <T> ApiResponse<T> errorStatus(MessageCode message) {
        return new ApiResponse<T>(APIConstants.ERROR_CODE, message, HttpStatus.BAD_REQUEST);
    }



    @Getter
    @Setter
    @Builder
    public static class APIBody<T> {
        int code;
        String errorCode;
        T data;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        T pageNo;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        T pageSize;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        T totalPages;
    }
}