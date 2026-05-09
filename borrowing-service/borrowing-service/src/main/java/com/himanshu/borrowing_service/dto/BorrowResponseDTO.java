package com.himanshu.borrowing_service.dto;

import com.himanshu.borrowing_service.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BorrowResponseDTO(
        Long id,
        Long userId,
        Long equipmentId,
        int quantity,
        LocalDateTime borrowDate,
        LocalDateTime returnDate,
        LocalDateTime dueDate,
        Status status
) {
}
