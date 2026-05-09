package com.himanshu.equipment_service.service;

import com.himanshu.equipment_service.dto.EquipmentRequestDTO;
import com.himanshu.equipment_service.dto.EquipmentResponseDTO;
import com.himanshu.equipment_service.entity.Equipment;
import com.himanshu.equipment_service.entity.EquipmentStatus;
import com.himanshu.equipment_service.mapper.EquipmentMapper;
import com.himanshu.equipment_service.repository.EquipmentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    @Transactional
    public EquipmentResponseDTO addEquipment(EquipmentRequestDTO dto) {

        if(dto.name() == null || dto.name().isEmpty()){
            throw new IllegalArgumentException("Equipment name cannot be null or empty");
        }

        if(equipmentRepository.findByName(dto.name()).isPresent()){
            throw new RuntimeException("Equipment already exists");
        }

        Equipment equipment = equipmentMapper.toEntity(dto);

        if(equipment.getTotalQuantity() <= 0){
            equipment.setStatus(EquipmentStatus.OUT_OF_STOCK);
        } else {
            equipment.setStatus(EquipmentStatus.AVAILABLE);
        }

        Equipment savedEquipment = equipmentRepository.save(equipment);

        return equipmentMapper.toResponseDTO(savedEquipment);
    }

    public List<EquipmentResponseDTO> getAllEquipment(){
        return equipmentRepository.findAll().stream()
                .map(equipmentMapper::toResponseDTO)
                .toList();
    }

    public EquipmentResponseDTO getEquipmentById(Long id){
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));
        return equipmentMapper.toResponseDTO(equipment);
    }

    @Transactional
    public EquipmentResponseDTO updateEquipment(Long id, EquipmentRequestDTO dto){
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));


        if(dto.name() != null && !dto.name().isBlank()){
            Optional<Equipment> existingEquipment = equipmentRepository.findByName(dto.name());

            existingEquipment.ifPresent(existing -> {
                if(existing.getId() != id) throw new RuntimeException("Equipment with name " + dto.name() + " already exists");
            });

            equipment.setName(dto.name());
        }

        if(dto.branchId() != null && !dto.branchId().isBlank()){
            equipment.setBranchId(dto.branchId());
        }
        if(dto.branchName() !=null && !dto.branchName().isBlank()){
            equipment.setBranchName(dto.branchName());
        }
        if(dto.totalQuantity()>=0){
            equipment.setTotalQuantity(dto.totalQuantity());
        }

        if(equipment.getTotalQuantity() <= 0){
            equipment.setStatus(EquipmentStatus.OUT_OF_STOCK);
        } else {
            equipment.setStatus(EquipmentStatus.AVAILABLE);
        }
        equipment.setUpdatedAt(LocalDateTime.now());

        Equipment updatedEquipment = equipmentRepository.save(equipment);

        return equipmentMapper.toResponseDTO(updatedEquipment);
    }

    @Transactional
    public void deleteEquipment(Long id){
        if(!equipmentRepository.existsById(id)){
            throw new RuntimeException("Equipment not found with id: " + id);
        }
        equipmentRepository.deleteById(id);
    }

    @Transactional
    public EquipmentResponseDTO borrowEquipmentStock(Long id, int quantity){

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));

        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if(equipment.getStatus() == EquipmentStatus.MAINTENANCE){
            throw new RuntimeException("Equipment is already maintenance");
        }

        if(equipment.getTotalQuantity() < quantity){
            throw new RuntimeException("Not enough stock available");
        }

        equipment.setTotalQuantity(equipment.getTotalQuantity() - quantity);

        if (equipment.getTotalQuantity() <= 0) {
            equipment.setStatus(EquipmentStatus.OUT_OF_STOCK);
        } else {
            equipment.setStatus(EquipmentStatus.AVAILABLE);
        }

        equipment.setUpdatedAt(LocalDateTime.now());

        Equipment savedEquipment = equipmentRepository.save(equipment);

        return equipmentMapper.toResponseDTO(savedEquipment);
    }

    @Transactional
    public EquipmentResponseDTO returnEquipmentStock(Long id, int quantity) {

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        equipment.setTotalQuantity(equipment.getTotalQuantity() + quantity);

        if (equipment.getStatus() != EquipmentStatus.MAINTENANCE) {
            equipment.setStatus(EquipmentStatus.AVAILABLE);
        }

        equipment.setUpdatedAt(LocalDateTime.now());

        Equipment savedEquipment = equipmentRepository.save(equipment);

        return equipmentMapper.toResponseDTO(savedEquipment);
    }
}
