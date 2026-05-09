package com.himanshu.borrowing_service.client;

import com.himanshu.borrowing_service.dto.EquipmentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "equipment-service", url = "http://localhost:8082")
public interface EquipmentClient {

    @GetMapping("/api/equipment{id}")
    EquipmentResponseDTO getEquipmentById(@PathVariable Long id);

    @PutMapping("/api/equipment/{id}/borrow")
    EquipmentResponseDTO borrowEquipmentStock(@PathVariable ("id") Long id, @RequestParam("quantity") int quality);

    @PutMapping("/api/equipment/{id}/return")
    EquipmentResponseDTO returnEquipmentStock(@PathVariable ("id") Long id, @RequestParam("quantity") int quality);

    @PutMapping("/api/equipment/{id}/")
    EquipmentResponseDTO updateEquipment(@PathVariable ("id") Long id, @RequestParam("quantity") int quality);



}
