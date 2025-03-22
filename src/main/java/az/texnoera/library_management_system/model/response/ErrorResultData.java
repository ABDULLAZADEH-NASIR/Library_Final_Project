package az.texnoera.library_management_system.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResultData {
    private int code;
    private String message;
    private List<String> errors;
}
