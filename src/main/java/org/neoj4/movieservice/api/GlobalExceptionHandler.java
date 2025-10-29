package org.neoj4.movieservice.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.neoj4.movieservice.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        log.error("Runtime Exception {} " , ex.getMessage() , ex);
        return build(ex , request , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex , HttpServletRequest request) {
        log.error("Exception {} " , ex.getMessage() , ex);
        return build(ex , request , HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<ErrorResponse> build(Exception ex, HttpServletRequest request, HttpStatus status) {
        ErrorResponse body = new ErrorResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage() ,
                request.getRequestURI(),
                status.value() ,
                LocalDateTime.now()

        );

        return ResponseEntity.status(status).body(body);
    }
}
