package az.texnoera.library_management_system.handler;

import az.texnoera.library_management_system.exception.ApiException;
import az.texnoera.library_management_system.model.response.ErrorResult;
import az.texnoera.library_management_system.model.response.ErrorResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionsHandle {

    // Bütün exceptionlari handle edir
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> ApiException(ApiException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(new ErrorResult(e.getStatusCode().getCode(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResult> RunTimeExceptionsHandle(RuntimeException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult(500, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResultData> MethodArgumentNotValidExHandle(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            log.error(error.getDefaultMessage());
            errors.add(error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ErrorResultData(500, "Field not valid", errors));
    }
}
