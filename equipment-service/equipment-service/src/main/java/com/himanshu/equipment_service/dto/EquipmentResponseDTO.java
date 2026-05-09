package com.himanshu.equipment_service.dto;

import com.himanshu.equipment_service.entity.EquipmentStatus;

import java.time.LocalDateTime;

public record EquipmentResponseDTO(

        Long id,
        String name,
        String branchId,
        String branchName,
        int totalQuantity,
        EquipmentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {
}
