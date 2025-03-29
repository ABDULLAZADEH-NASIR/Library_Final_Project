package az.texnoera.library_management_system.repo;

import az.texnoera.library_management_system.entity.BorrowBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowBookRepo extends JpaRepository<BorrowBook, Long> {
    @Query("SELECT DISTINCT b FROM BorrowBook b LEFT JOIN FETCH b.user")
    Page<BorrowBook> findAllBorrowBooks(Pageable pageable);

    @Query("SELECT b FROM BorrowBook b LEFT JOIN FETCH b.user WHERE b.id=:id")
    Optional<BorrowBook> findBorrowBookById(Long id);

}
