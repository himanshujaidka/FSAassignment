package com.himanshu.borrowing_service.mapper;

import com.himanshu.borrowing_service.dto.BorrowRequestDTO;
import com.himanshu.borrowing_service.dto.BorrowResponseDTO;
import com.himanshu.borrowing_service.entity.BorrowRecord;
import org.springframework.stereotype.Component;

@Component
public class BorrowMapper {

    public BorrowRecord toEntity(BorrowRequestDTO dto){
        BorrowRecord record = new BorrowRecord();
        record.setUserId(dto.userId());
        record.setEquipmentId(dto.equipmentId());
        record.setQuantity(dto.quantity());
        record.setBorrowDays(dto.borrowDays());
        return record;
    }

    public BorrowResponseDTO toBorrowResponseDTO(BorrowRecord record){
        return new BorrowResponseDTO(
                record.getId(),
                record.getUserId(),
                record.getEquipmentId(),
                record.getQuantity(),
                record.getBorrowDate(),
                record.getReturnDate(),
                record.getDueDate(),
                record.getStatus()
        );
    }
}
