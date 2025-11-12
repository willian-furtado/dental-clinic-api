package com.sboot.api.dental_clinic_api.controller;

import com.sboot.api.dental_clinic_api.dto.AppointmentDTO;
import com.sboot.api.dental_clinic_api.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public Page<AppointmentDTO> getAll(Pageable pageable) {
        return appointmentService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public AppointmentDTO getById(@PathVariable String id) {
        return appointmentService.getById(id);
    }

    @PostMapping
    public AppointmentDTO create(@RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.save(appointmentDTO);
    }

    @PutMapping("/{id}")
    public AppointmentDTO update(@PathVariable String id, @RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.update(id, appointmentDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        appointmentService.delete(id);
    }
}
