package com.himanshu.borrowing_service.controller;

import com.himanshu.borrowing_service.dto.BorrowRequestDTO;
import com.himanshu.borrowing_service.dto.BorrowResponseDTO;
import com.himanshu.borrowing_service.service.BorrowService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@AllArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping
    public BorrowResponseDTO borrowEquipmentStock(BorrowRequestDTO dto) {
        return borrowService.borrowEquipment(dto);
    }

    @PutMapping("/return")
    public BorrowResponseDTO returnEquipment(@PathVariable Long id) {

        return borrowService.returnEquipment(id);
    }


}
