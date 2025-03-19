package az.texnoera.library_management_system.controller;

import az.texnoera.library_management_system.service.concrets.BorrowBookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrows")
public class BorrowBookController {
    private final BorrowBookServiceImpl borrowBookService;
}
