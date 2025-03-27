package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.entity.BorrowBook;
import az.texnoera.library_management_system.model.request.BorrowBookRequest;
import az.texnoera.library_management_system.model.response.BorrowBookResponse;
import az.texnoera.library_management_system.model.response.Result;
import az.texnoera.library_management_system.service.concrets.BorrowBookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrows")
public class BorrowBookController {
    private final BorrowBookServiceImpl borrowBookService;

    @GetMapping
    public Result<BorrowBookResponse> getAllBorrowBooks(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return borrowBookService.getAllBorrows(page, size);
    }

    @GetMapping("/search-borrowId/{id}")
    public BorrowBookResponse getBorrowBookById(@PathVariable Long id) {
        return borrowBookService.getBorrowById(id);
    }

    @PostMapping("/create")
    public BorrowBookResponse addBorrowBook(@RequestBody BorrowBookRequest borrowBookRequest) {
        return borrowBookService.createBorrow(borrowBookRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBorrowBook(@PathVariable Long id) {
        borrowBookService.deleteBorrowByBorrowId(id);
    }

}
