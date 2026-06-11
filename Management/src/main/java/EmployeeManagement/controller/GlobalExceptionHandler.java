package EmployeeManagement.controller;

import EmployeeManagement.exception.AppException;
import EmployeeManagement.exception.ErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

import static EmployeeManagement.exception.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        return build(ex.getStatus(), ex.getErrorCode() != null
                ? new EmployeeManagement.exception.ErrorCode[] {ex.getErrorCode()}[0]
                : INTERNAL_SERVER_ERROR,
                ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        String msg = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        if (msg != null && msg.toLowerCase().contains("email")) {
            return build(HttpStatus.CONFLICT, EMPLOYEE_EMAIL_DUPLICATE,
                    "An employee with this email already exists.");
        }
        return build(HttpStatus.CONFLICT, DATABASE_ERROR,
                "A data conflict occurred. Please check your input.");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, DATABASE_ERROR,
                "Unable to reach the database. Please try again later.");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .findFirst().orElse("Validation failed.");
        return build(HttpStatus.BAD_REQUEST, INVALID_INPUT, msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, INVALID_INPUT,
                "Request body is missing or unreadable.");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, INVALID_INPUT,
                "Unsupported content type: " + ex.getContentType()
                + ". Expected application/xml.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, INVALID_INPUT,
                "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        return build(HttpStatus.NOT_FOUND, EMPLOYEE_NOT_FOUND,
                "The requested endpoint does not exist: " + ex.getRequestURL());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, INVALID_INPUT, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.");
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            EmployeeManagement.exception.ErrorCode code,
            String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status.value(), code, message));
    }
}