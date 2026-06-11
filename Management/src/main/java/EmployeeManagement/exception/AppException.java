package EmployeeManagement.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus status;

    public AppException(ErrorCode errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status    = status;
    }

    public ErrorCode getErrorCode() { return errorCode; }
    public HttpStatus getStatus()   { return status; }


    public static AppException employeeNotFound(Long id) {
        return new AppException(
            ErrorCode.EMPLOYEE_NOT_FOUND,
            "Employee not found with ID: " + id,
            HttpStatus.NOT_FOUND
        );
    }

    public static AppException emailDuplicate(String email) {
        return new AppException(
            ErrorCode.EMPLOYEE_EMAIL_DUPLICATE,
            "An employee with email '" + email + "' already exists.",
            HttpStatus.CONFLICT
        );
    }

    public static AppException invalidXml(String detail) {
        return new AppException(
            ErrorCode.INVALID_XML,
            "Invalid XML format: " + detail,
            HttpStatus.BAD_REQUEST
        );
    }

    public static AppException xmlParseError(String detail) {
        return new AppException(
            ErrorCode.XML_PARSE_ERROR,
            "Failed to parse XML: " + detail,
            HttpStatus.BAD_REQUEST
        );
    }

    public static AppException invalidInput(String detail) {
        return new AppException(
            ErrorCode.INVALID_INPUT,
            "Invalid input: " + detail,
            HttpStatus.BAD_REQUEST
        );
    }

    public static AppException unauthorized(String detail) {
        return new AppException(
            ErrorCode.UNAUTHORIZED,
            detail,
            HttpStatus.UNAUTHORIZED
        );
    }

    public static AppException tokenExpired() {
        return new AppException(
            ErrorCode.TOKEN_EXPIRED,
            "Your session has expired. Please log in again.",
            HttpStatus.UNAUTHORIZED
        );
    }

    public static AppException tokenMissing() {
        return new AppException(
            ErrorCode.TOKEN_MISSING,
            "Authorization token is missing. Please log in.",
            HttpStatus.UNAUTHORIZED
        );
    }

    public static AppException databaseError(String detail) {
        return new AppException(
            ErrorCode.DATABASE_ERROR,
            "A database error occurred: " + detail,
            HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    public static AppException internalError(String detail) {
        return new AppException(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "An unexpected server error occurred: " + detail,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}