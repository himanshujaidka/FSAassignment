package com.himanshu.borrowing_service.repository;

import com.himanshu.borrowing_service.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByUserId(Long userId);

    List<BorrowRecord> findByEquipmentId(Long equipmentId);

    Optional<BorrowRecord> findByIdAndStatus(Long id, String status);

    List<BorrowRecord> findByStatus(String status);
}
