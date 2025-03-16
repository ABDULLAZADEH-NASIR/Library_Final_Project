package az.texnoera.library_management_system.repo;

import az.texnoera.library_management_system.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long> {
    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books")
    Page<Author> findAllAuthors( Pageable pageable);

    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books WHERE a.id=:id")
    Optional<Author> findByAuthorId(Long id);

    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books where a.name=:name")
    Optional<Author> findByAuthorName(String name);
}
