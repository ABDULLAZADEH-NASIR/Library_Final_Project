package az.texnoera.library_management_system.entity;

import az.texnoera.library_management_system.model.enums.BookCategory;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private int year;
    @NotNull
    private int pages;
    @NotNull
    private Long totalBooksCount;
    @NotNull
    private Long avialableBooksCount;
    @Nullable
    @Enumerated(EnumType.STRING)
    private BookCategory category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable
    private Set<Author> authors = new HashSet<>();

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BorrowBook> borrowBook = new HashSet<>();

}
