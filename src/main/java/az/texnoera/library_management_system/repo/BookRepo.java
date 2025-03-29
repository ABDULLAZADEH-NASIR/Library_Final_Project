package az.texnoera.library_management_system.repo;

import az.texnoera.library_management_system.entity.Book;
import az.texnoera.library_management_system.model.enums.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    @Query("SELECT DISTINCT b from Book b LEFT  JOIN  FETCH  b.authors ")
    Page<Book> findAllBooks(Pageable pageable);

    @Query("SELECT DISTINCT  b  FROM Book b LEFT JOIN FETCH b.authors where b.id=:id")
    Optional<Book> findBookById(Long id);

    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN  FETCH b.authors where b.name=:name")
    Optional<Book> findBookByName(String name);

    @Query("SELECT  DISTINCT  b FROM Book b LEFT JOIN FETCH b.authors where b.category=:category")
    Page<Book> findABookByCategory(BookCategory category, Pageable pageable);

}
