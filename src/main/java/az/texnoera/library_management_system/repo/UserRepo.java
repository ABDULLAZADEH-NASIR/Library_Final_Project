package az.texnoera.library_management_system.repo;

import az.texnoera.library_management_system.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.borrowedBooks WHERE u.id=:id")
    Optional<User> findUserWithBorrow(Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.borrowedBooks ORDER BY u.name ASC ")
    Page<User> findAllUsers(Pageable pageable);

    @Query("SELECT DISTINCT  u FROM User u LEFT JOIN FETCH u.borrowedBooks WHERE u.FIN=:fin")
    Optional<User> findUserByFIN(String fin);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.borrowedBooks")
    Set<User> findAllUsersWithBorrowedBooks();
}
