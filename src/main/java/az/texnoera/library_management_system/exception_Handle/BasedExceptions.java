package az.texnoera.library_management_system.exception_Handle;

import az.texnoera.library_management_system.model.enums.StatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasedExceptions extends RuntimeException {
    private HttpStatus httpStatusCode;
    private StatusCode statusCode;

    public BasedExceptions(HttpStatus httpStatusCode1,
                           StatusCode statusCode1) {
        super(statusCode1.getMessage());
        this.httpStatusCode = httpStatusCode1;
        this.statusCode = statusCode1;
    }

}
