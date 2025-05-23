package az.texnoera.library_management_system.repo;

import az.texnoera.library_management_system.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query("""
                SELECT DISTINCT u FROM User u 
                LEFT JOIN FETCH u.bookCheckouts bc
                LEFT JOIN FETCH bc.book
                WHERE u.id = :id
            """)
    Optional<User> findUserWithBorrow(Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.bookCheckouts" +
            " JOIN u.roles r WHERE r.name = 'ROLE_USER' ORDER BY u.name ASC")
    Page<User> findAllUsers(Pageable pageable);

    @Query("""
                SELECT DISTINCT u FROM User u 
                LEFT JOIN FETCH u.bookCheckouts bc 
                LEFT JOIN FETCH bc.book 
                WHERE u.FIN = :fin
            """)
    Optional<User> findUserByFIN(String fin);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.bookCheckouts")
    List<User> findAllUsersWithBorrowedBooks();

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles  WHERE u.email = :email")
    Optional<User> findByEmail(@NotNull String email);

    @Query("""
                SELECT DISTINCT u FROM User u\s
                LEFT JOIN FETCH u.roles\s
                LEFT JOIN FETCH u.bookCheckouts bc\s
                LEFT JOIN FETCH bc.book b\s
                LEFT JOIN FETCH b.authors\s
                WHERE u.email = :email
            """)
    Optional<User> findUserByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.FIN = :fin AND u.email = :email")
    boolean existsByFinAndEmail(String fin, String email);
}
