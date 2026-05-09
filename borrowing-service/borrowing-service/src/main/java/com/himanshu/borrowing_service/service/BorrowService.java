package com.himanshu.borrowing_service.service;

import com.himanshu.borrowing_service.client.EquipmentClient;
import com.himanshu.borrowing_service.dto.BorrowRequestDTO;
import com.himanshu.borrowing_service.dto.BorrowResponseDTO;
import com.himanshu.borrowing_service.dto.EquipmentResponseDTO;
import com.himanshu.borrowing_service.entity.BorrowRecord;
import com.himanshu.borrowing_service.enums.Status;
import com.himanshu.borrowing_service.mapper.BorrowMapper;
import com.himanshu.borrowing_service.repository.BorrowRecordRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final EquipmentClient equipmentClient;
    private final BorrowMapper borrowMapper;

    public BorrowResponseDTO borrowEquipment(BorrowRequestDTO dto) {

        if(dto.userId() == null || dto.equipmentId() == null){
            throw new IllegalArgumentException("User ID and Equipment ID must be provided");
        }

        if(dto.quantity() <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if(dto.borrowDays() <=0 ){
            throw new IllegalArgumentException("Borrow days must be greater than zero");
        }

        EquipmentResponseDTO equipment = equipmentClient.getEquipmentById(dto.equipmentId());

        if(equipment == null){
            throw new IllegalArgumentException("Equipment not found with ID: " + dto.equipmentId());
        }

        if("MAINTENANCE".equals(equipment.status())){
            throw new IllegalStateException("Equipment is currently under maintenance and cannot be borrowed");
        }

        if(equipment.totalQuantity() <=0){
            throw new IllegalStateException("Equipment is out of stock");
        }

        if(dto.quantity() > equipment.totalQuantity()){
            throw new IllegalStateException("Requested quantity exceeds available stock");
        }


        equipmentClient.borrowEquipmentStock(dto.equipmentId(), dto.quantity());

        BorrowRecord record = borrowMapper.toEntity(dto);

        LocalDateTime now = LocalDateTime.now();

        record.setBorrowDate(now);
        record.setDueDate(now.plusDays(dto.borrowDays()));
        record.setStatus(Status.BORROWED);

        BorrowRecord savedRecord = borrowRecordRepository.save(record);

        return borrowMapper.toBorrowResponseDTO(savedRecord);
    }

    @Transactional
    public BorrowResponseDTO returnEquipment(Long borrowId) {

        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() ->
                        new RuntimeException("Borrow record not found with id: " + borrowId));

        if (record.getStatus() != Status.BORROWED
                && record.getStatus() != Status.OVERDUE) {
            throw new IllegalStateException(
                    "Borrow record is not currently active");
        }

        EquipmentResponseDTO equipment = equipmentClient.getEquipmentById(
                record.getEquipmentId());

        if (equipment == null) {
            throw new RuntimeException(
                    "Equipment not found with id: " + record.getEquipmentId());
        }

        equipmentClient.returnEquipmentStock(
                record.getEquipmentId(),
                record.getQuantity()
        );

        LocalDateTime now = LocalDateTime.now();

        record.setReturnDate(now);

        if (record.getDueDate() != null && now.isAfter(record.getDueDate())) {
            record.setStatus(Status.OVERDUE);
        } else {
            record.setStatus(Status.RETURNED);
        }

        record.setUpdatedAt(now);

        BorrowRecord updatedRecord = borrowRecordRepository.save(record);

        return borrowMapper.toBorrowResponseDTO(
                updatedRecord
        );
    }
}
