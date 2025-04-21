package az.texnoera.library_management_system.exception;

import az.texnoera.library_management_system.model.enums.StatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
// Exception handle-də istifadə olunur
public class ApiException extends RuntimeException {
    private HttpStatus httpStatusCode;
    private StatusCode statusCode;

    public ApiException(HttpStatus httpStatusCode1,
                        StatusCode statusCode1) {
        super(statusCode1.getMessage());
        this.httpStatusCode = httpStatusCode1;
        this.statusCode = statusCode1;
    }
}
