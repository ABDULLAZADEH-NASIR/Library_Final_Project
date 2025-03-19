package az.texnoera.library_management_system.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequestForUpdate {
    private String name;
    private int year;
    private Long totalBookCount;
}
