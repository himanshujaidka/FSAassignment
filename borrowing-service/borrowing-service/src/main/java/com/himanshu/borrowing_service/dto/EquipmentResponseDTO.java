package com.himanshu.borrowing_service.dto;

import java.time.LocalDateTime;

public record EquipmentResponseDTO(

        Long id,
        String name,
        String branchId,
        String branchName,
        int totalQuantity,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {
}
