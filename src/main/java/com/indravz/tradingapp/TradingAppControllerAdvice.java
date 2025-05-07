package com.indravz.tradingapp;

import com.indravz.tradingapp.model.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class TradingAppControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(HttpServletRequest request, Exception ex) {
        // Get the RequestId from the request attributes (set in the filter)
        String requestId = (String) request.getAttribute("requestId");

        ApiError apiError = new ApiError(requestId, "An unexpected error occurred");
        log.error("Encountered unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    /*// Handle specific exception like Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException ex) {
        String requestId = (String) request.getAttribute("requestId");
        ApiError apiError = new ApiError(requestId, "Resource not found: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    // Handle Unauthorized exception
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(HttpServletRequest request, UnauthorizedException ex) {
        String requestId = (String) request.getAttribute("requestId");
        ApiError apiError = new ApiError(requestId, ex.getMessage() != null ? ex.getMessage() : "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    // Handle Forbidden exception
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbiddenException(HttpServletRequest request, ForbiddenException ex) {
        String requestId = (String) request.getAttribute("requestId");
        ApiError apiError = new ApiError(requestId, ex.getMessage() != null ? ex.getMessage() : "Forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }*/
}
