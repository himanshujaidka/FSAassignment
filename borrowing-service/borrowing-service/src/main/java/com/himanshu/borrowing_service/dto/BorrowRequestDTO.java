package com.himanshu.borrowing_service.dto;

public record BorrowRequestDTO(
        Long userId,
        Long equipmentId,
        int quantity,
        int borrowDays
) {
}
