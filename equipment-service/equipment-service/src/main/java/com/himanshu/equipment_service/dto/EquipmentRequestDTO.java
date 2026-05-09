package com.himanshu.equipment_service.dto;

public record EquipmentRequestDTO(
        String name,
        String branchId,
        String branchName,
        int totalQuantity
) {
}
