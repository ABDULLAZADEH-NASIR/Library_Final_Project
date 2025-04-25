package az.texnoera.library_management_system.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
// Book kateqoriyaları
public enum BookCategory {
    DETECTIVE,
    SCIENCE,
    ROMANCE,
    DRAMA,
    HISTORY,
    PROGRAMMING;
}
