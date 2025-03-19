package az.texnoera.library_management_system.repo;

import az.texnoera.library_management_system.entity.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowBookRepo extends JpaRepository<BorrowBook, Long> {
}
