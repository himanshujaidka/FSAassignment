package com.himanshu.equipment_service.repository;

import com.himanshu.equipment_service.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByBranchId(String branchId);

    List<Equipment> findByStatus(String status);

    Optional<Equipment> findByName(String name);

}
