package az.texnoera.library_management_system.service.concrets;

import az.texnoera.library_management_system.repo.BorrowBookRepo;
import az.texnoera.library_management_system.service.abstracts.BorrowBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowBookServiceImpl implements BorrowBookService {
    private final BorrowBookRepo borrowBookRepo;
}
