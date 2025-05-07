package com.indravz.tradingapp.model;

import java.time.LocalDateTime;

public class ApiError {

    private String requestId;
    private String message;
    private LocalDateTime timestamp;

    public ApiError(String requestId, String message) {
        this.requestId = requestId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
