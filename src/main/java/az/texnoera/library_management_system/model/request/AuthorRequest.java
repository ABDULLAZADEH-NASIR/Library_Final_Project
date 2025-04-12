package az.texnoera.library_management_system.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorRequest {
    private String name;
    private String surname;
}
