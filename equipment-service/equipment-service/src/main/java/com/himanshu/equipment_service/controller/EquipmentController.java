package com.himanshu.equipment_service.controller;

import com.himanshu.equipment_service.dto.EquipmentRequestDTO;
import com.himanshu.equipment_service.dto.EquipmentResponseDTO;
import com.himanshu.equipment_service.service.EquipmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@AllArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipmentResponseDTO> addEquipment(@RequestBody EquipmentRequestDTO dto) {

       return ResponseEntity.ok(equipmentService.addEquipment(dto));
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @GetMapping("/{id}")
    public EquipmentResponseDTO getEquipmentById(@PathVariable Long id) {
        return equipmentService.getEquipmentById(id);
    }

    @PutMapping ("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipmentResponseDTO> updateEquipment(@PathVariable Long id, @RequestBody EquipmentRequestDTO dto) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
    }

    @PutMapping("/{id}/borrow")
    public EquipmentResponseDTO borrowEquipmentStock(@PathVariable Long id, @RequestParam("quantity") int quantity) {
        return equipmentService.borrowEquipmentStock(id, quantity);
    }

    @PutMapping("/{id}/return")
    public EquipmentResponseDTO returnEquipmentStock(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {
        return equipmentService.returnEquipmentStock(id, quantity);
    }
}
