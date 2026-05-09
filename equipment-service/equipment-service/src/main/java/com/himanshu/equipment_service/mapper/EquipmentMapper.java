package com.himanshu.equipment_service.mapper;

import com.himanshu.equipment_service.dto.EquipmentRequestDTO;
import com.himanshu.equipment_service.dto.EquipmentResponseDTO;
import com.himanshu.equipment_service.entity.Equipment;
import org.springframework.stereotype.Controller;

@Controller
public class EquipmentMapper {

    public Equipment toEntity(EquipmentRequestDTO dto){
        Equipment equipment = new Equipment();
        equipment.setName(dto.name());
        equipment.setBranchId(dto.branchId());
        equipment.setBranchName(dto.branchName());
        equipment.setTotalQuantity(dto.totalQuantity());
        return equipment;
    }

    public EquipmentResponseDTO toResponseDTO(Equipment equipment){
        return new EquipmentResponseDTO(
                equipment.getId(),
                equipment.getName(),
                equipment.getBranchId(),
                equipment.getBranchName(),
                equipment.getTotalQuantity(),
                equipment.getStatus(),
                equipment.getCreatedAt(),
                equipment.getUpdatedAt()
        );
    }
}
