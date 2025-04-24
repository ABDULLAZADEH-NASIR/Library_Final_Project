package az.texnoera.library_management_system.repo;

import az.texnoera.library_management_system.entity.BookCheckout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookCheckoutRepo extends JpaRepository<BookCheckout, Long> {
    @Query("SELECT DISTINCT b FROM BookCheckout b LEFT JOIN FETCH b.user")
    Page<BookCheckout> findAllBookCheckouts(Pageable pageable);

    @Query("""
                SELECT b FROM BookCheckout b
                LEFT JOIN FETCH b.user
                LEFT JOIN FETCH b.book
                WHERE b.id = :id
            """)
    Optional<BookCheckout> findBookCheckoutById(Long id);

    @Query("SELECT DISTINCT b FROM BookCheckout b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.book " +
            "WHERE b.isCollected=false AND b.checkoutDate < :time")
    Set<BookCheckout> findExpiredUncollectedWithUserAndBook(@Param("time") LocalDateTime time);

}
