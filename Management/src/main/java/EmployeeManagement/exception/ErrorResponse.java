package EmployeeManagement.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final int        status;
    private final String     errorCode;
    private final String     message;
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, ErrorCode errorCode, String message) {
        this.status    = status;
        this.errorCode = errorCode.name();
        this.message   = message;
        this.timestamp = LocalDateTime.now();
    }

    public int           getStatus()    { return status; }
    public String        getErrorCode() { return errorCode; }
    public String        getMessage()   { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}